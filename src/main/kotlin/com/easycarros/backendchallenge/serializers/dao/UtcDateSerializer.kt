package com.easycarros.backendchallenge.serializers.dao

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDate
import java.time.ZoneOffset

class UtcDateSerializer : JsonSerializer<LocalDate>() {
    override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeNumber(value.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())
    }
}
