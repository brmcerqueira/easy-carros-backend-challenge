package com.easycarros.backendchallenge.serializers.rest

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeSerializer : JsonSerializer<LocalDateTime>() {
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").format(value))
    }
}