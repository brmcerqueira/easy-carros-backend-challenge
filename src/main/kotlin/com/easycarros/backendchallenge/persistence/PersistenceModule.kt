package com.easycarros.backendchallenge.persistence

import com.brmcerqueira.kuerongo.config.KuerongoConfig
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.easycarros.backendchallenge.common.Auditor
import com.easycarros.backendchallenge.serializers.SensitiveStringSerializer
import com.easycarros.backendchallenge.serializers.dao.*
import com.easycarros.backendchallenge.serializers.registerDeserializer
import com.easycarros.backendchallenge.serializers.registerSerializer
import dagger.Module
import dagger.Provides
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.mongo.MongoClient
import javax.inject.Singleton
import javax.inject.Named

@Module
class PersistenceModule {
    @Provides
    @Singleton
    fun provideMongoClient(vertx: Vertx, @Named("config") config: JsonObject): MongoClient =
        MongoClient.createShared(vertx, config.getJsonObject("mongo"))

    @Provides
    @Singleton
    @Named("persistence")
    fun provideAuditor() = Auditor("persistence")

    @Provides
    @Singleton
    @Named("persistence")
    fun provideObjectMapper(): ObjectMapper {
        val module = SimpleModule()

        module.registerSerializer(JsonObjectSerializer())
        module.registerSerializer(JsonArraySerializer())
        module.registerSerializer(UtcDateSerializer())
        module.registerDeserializer(UtcDateDeserializer())
        module.registerSerializer(UtcDateTimeSerializer())
        module.registerDeserializer(UtcDateTimeDeserializer())
        module.registerSerializer(UtcTimeSerializer())
        module.registerDeserializer(UtcTimeDeserializer())
        module.registerSerializer(SensitiveStringSerializer(true))

        val objectMapper = ObjectMapper().registerKotlinModule().registerModule(module).configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

        KuerongoConfig.use(VertxKuerongoProvider(objectMapper))

        return objectMapper
    }
}
