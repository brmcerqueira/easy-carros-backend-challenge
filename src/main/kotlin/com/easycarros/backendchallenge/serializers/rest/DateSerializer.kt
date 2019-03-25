package com.easycarros.backendchallenge.serializers.rest

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateSerializer : JsonSerializer<LocalDate>() {
    override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd'T00:00:00'").format(value))
    }
}