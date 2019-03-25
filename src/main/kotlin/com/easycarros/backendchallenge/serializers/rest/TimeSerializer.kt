package com.easycarros.backendchallenge.serializers.rest

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimeSerializer : JsonSerializer<LocalTime>() {
    override fun serialize(value: LocalTime, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeString(DateTimeFormatter.ofPattern("HH:mm:ss").format(value))
    }
}