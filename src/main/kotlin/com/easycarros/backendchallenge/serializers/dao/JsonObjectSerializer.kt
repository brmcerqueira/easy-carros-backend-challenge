package com.easycarros.backendchallenge.serializers.dao

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.vertx.core.json.JsonObject

class JsonObjectSerializer : JsonSerializer<JsonObject>() {
    override fun serialize(value: JsonObject, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeObject(value.map)
    }
}
