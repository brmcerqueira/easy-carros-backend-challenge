package com.easycarros.backendchallenge.consumers

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.easycarros.backendchallenge.serializers.SensitiveStringSerializer
import com.easycarros.backendchallenge.serializers.registerDeserializer
import com.easycarros.backendchallenge.serializers.registerSerializer
import com.easycarros.backendchallenge.serializers.rest.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsumeObjectMapperManager @Inject constructor() {
    private val map = HashMap<String, ObjectMapper>()

    private fun createObjectMapper(): ObjectMapper {
        val module = SimpleModule()

        module.registerSerializer(DateSerializer())
        module.registerDeserializer(DateDeserializer())
        module.registerSerializer(DateTimeSerializer())
        module.registerDeserializer(DateTimeDeserializer())
        module.registerSerializer(TimeSerializer())
        module.registerDeserializer(TimeDeserializer())
        module.registerSerializer(SensitiveStringSerializer())

        return ObjectMapper().registerKotlinModule().registerModule(module).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    }

    internal fun registerModuleForAll(module: SimpleModule) {
        map.values.forEach { it.registerModule(module) }
    }

    operator fun get(key: String): ObjectMapper = if (map.containsKey(key)) map[key]!! else {
        val objectMapper = createObjectMapper()
        map[key] = objectMapper
        objectMapper
    }
}