package com.easycarros.backendchallenge.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

class EnumSerializer<T : Enum<T>> : JsonSerializer<T>() {
    override fun serialize(value: T, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeNumber(value.ordinal)
    }
}