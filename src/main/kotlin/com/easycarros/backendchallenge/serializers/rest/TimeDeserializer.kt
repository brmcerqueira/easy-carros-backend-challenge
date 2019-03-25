package com.easycarros.backendchallenge.serializers.rest

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalTime

class TimeDeserializer : JsonDeserializer<LocalTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): LocalTime =
            LocalTime.parse(p.codec.readValue(p, String::class.java))
}