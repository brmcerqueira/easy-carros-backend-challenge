package com.easycarros.backendchallenge.serializers.dao

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import io.vertx.core.json.JsonArray

class JsonArraySerializer : JsonSerializer<JsonArray>() {
    override fun serialize(value: JsonArray, jgen: JsonGenerator, provider: SerializerProvider) {
        jgen.writeObject(value.list)
    }
}
