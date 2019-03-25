package com.easycarros.backendchallenge.serializers.dao

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.ZoneOffset

class UtcDateTimeSerializer : JsonSerializer<LocalDateTime>() {
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeNumber(value.toInstant(ZoneOffset.UTC).toEpochMilli())
    }
}
