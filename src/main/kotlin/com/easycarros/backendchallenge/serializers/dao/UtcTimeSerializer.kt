package com.easycarros.backendchallenge.serializers.dao

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset

class UtcTimeSerializer : JsonSerializer<LocalTime>() {
    override fun serialize(value: LocalTime, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeNumber(value.atDate(LocalDate.EPOCH).toInstant(ZoneOffset.UTC).toEpochMilli())
    }
}
