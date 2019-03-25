package com.easycarros.backendchallenge.serializers.dao

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

class UtcDateDeserializer : JsonDeserializer<LocalDate>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDate =
    Instant.ofEpochMilli(p.codec.readValue(p, Long::class.java)).atZone(ZoneOffset.UTC).toLocalDate()
}
