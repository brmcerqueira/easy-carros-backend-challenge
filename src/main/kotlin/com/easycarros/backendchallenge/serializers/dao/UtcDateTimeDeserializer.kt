package com.easycarros.backendchallenge.serializers.dao

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class UtcDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDateTime =
        Instant.ofEpochMilli(p.codec.readValue(p, Long::class.java)).atZone(ZoneOffset.UTC).toLocalDateTime()
}
