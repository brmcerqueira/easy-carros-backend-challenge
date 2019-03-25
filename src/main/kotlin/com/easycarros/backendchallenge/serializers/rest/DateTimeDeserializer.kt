package com.easycarros.backendchallenge.serializers.rest

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalDateTime =
            LocalDateTime.parse(p.codec.readValue(p, String::class.java), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss[.SSS'Z']"))
}