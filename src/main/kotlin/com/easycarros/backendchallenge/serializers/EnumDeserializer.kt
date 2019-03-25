package com.easycarros.backendchallenge.serializers

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

class EnumDeserializer<T : Enum<T>>(private val values: Array<T>) : JsonDeserializer<T>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext?): T  {
        val index = p.codec.readValue(p, Int::class.java)
        return values.first { it.ordinal == index }
    }
}