package com.easycarros.backendchallenge.serializers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.easycarros.backendchallenge.consumers.ConsumeObjectMapperManager
import com.easycarros.backendchallenge.serializers.rest.*
import io.vertx.core.json.Json
import javax.inject.Inject
import javax.inject.Named

class SerializerBuilder @Inject constructor(private val consumeObjectMapperManager: ConsumeObjectMapperManager,
                                            @Named("persistence") private val persistenceObjectMapper: ObjectMapper) {

    private var restAction: (SimpleModule.() -> Unit)? = null
    private var consumeAction: (SimpleModule.() -> Unit)? = null
    private var consumeSpecificAction: Pair<Array<String>, SimpleModule.() -> Unit>? = null
    private var daoAction: (SimpleModule.() -> Unit)? = null

    fun all(action: SimpleModule.() -> Unit) {
        restAction = action
        consumeAction = action
        daoAction = action
    }

    fun restAndConsume(action: SimpleModule.() -> Unit) {
        restAction = action
        consumeAction = action
    }

    fun consumeAndDao(action: SimpleModule.() -> Unit) {
        consumeAction = action
        daoAction = action
    }

    fun rest(action: SimpleModule.() -> Unit) {
        restAction = action
    }

    fun consume(action: SimpleModule.() -> Unit) {
        consumeAction = action
    }

    fun consume(array: Array<String>, action: SimpleModule.() -> Unit) {
        consumeSpecificAction = Pair(array, action)
    }

    fun dao(action: SimpleModule.() -> Unit) {
        daoAction = action
    }

    internal fun build() {
        val restModule = SimpleModule()

        restModule.registerSerializer(DateSerializer())
        restModule.registerDeserializer(DateDeserializer())
        restModule.registerSerializer(DateTimeSerializer())
        restModule.registerDeserializer(DateTimeDeserializer())
        restModule.registerSerializer(TimeSerializer())
        restModule.registerDeserializer(TimeDeserializer())
        restModule.registerSerializer(SensitiveStringSerializer())

        restAction?.let { restModule.it() }

        Json.mapper.registerKotlinModule().registerModule(restModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        Json.prettyMapper.registerKotlinModule().registerModule(restModule).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        consumeAction?.let {
            val simpleModule = SimpleModule()
            simpleModule.it()
            consumeObjectMapperManager.registerModuleForAll(simpleModule)
        }

        consumeSpecificAction?.let { pair ->
            pair.first.forEach {
                val simpleModule = SimpleModule()
                pair.second(simpleModule)
                consumeObjectMapperManager[it].registerModule(simpleModule)
            }
        }

        daoAction?.let {
            val simpleModule = SimpleModule()
            simpleModule.it()
            persistenceObjectMapper.registerModule(simpleModule)
        }
    }
}
