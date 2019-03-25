package com.easycarros.backendchallenge.serializers

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer

class SensitiveStringSerializer(private val isPersistence: Boolean = false,
                                private val sensitive: Sensitive? = null) : StdSerializer<String>(String::class.java), ContextualSerializer {

    override fun serialize(value: String, gen: JsonGenerator, serializers: SerializerProvider?) {
        gen.writeString(if (sensitive != null && (!isPersistence || isPersistence && sensitive.securePersistence)) value.mapIndexed { i, c ->
            if (sensitive.value.isNotEmpty())
                if (i < sensitive.value.length && sensitive.value[i] != '#')
                    sensitive.value[i]
                else c
            else '*'
        }.joinToString("") else value)
    }

    override fun createContextual(prov: SerializerProvider?, property: BeanProperty?): JsonSerializer<*> {
        return SensitiveStringSerializer(isPersistence, property?.getAnnotation(Sensitive::class.java))
    }
}