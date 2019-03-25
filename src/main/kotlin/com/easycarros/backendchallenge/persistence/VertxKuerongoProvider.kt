package com.easycarros.backendchallenge.persistence

import com.brmcerqueira.kuerongo.config.IKuerongoProvider
import com.brmcerqueira.kuerongo.wrappers.IJsonArrayNativeWrapper
import com.brmcerqueira.kuerongo.wrappers.IJsonObjectNativeWrapper
import com.fasterxml.jackson.databind.ObjectMapper
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject

class VertxKuerongoProvider(private val jsonObjectMapper: ObjectMapper) : IKuerongoProvider {
    override fun createJsonObject(): IJsonObjectNativeWrapper = object : IJsonObjectNativeWrapper {
        private val jsonObject = JsonObject()

        override val raw: Any
            get() = jsonObject

        override val isEmpty: Boolean
            get() = jsonObject.isEmpty

        override fun <T> set(key: String, value: T) {
            jsonObject.put(key, jsonObjectMapper.convertValue(value, Any::class.java))
        }

        override fun toString(): String = jsonObject.toString()
    }

    override fun createJsonArray(): IJsonArrayNativeWrapper = object : IJsonArrayNativeWrapper {
        private val jsonArray = JsonArray()

        override val raw: Any
            get() = jsonArray

        override val isEmpty: Boolean
            get() = jsonArray.isEmpty

        override fun <T> add(value: T) {
            jsonArray.add(jsonObjectMapper.convertValue(value, Any::class.java))
        }

        override fun toString(): String = jsonArray.toString()
    }
}
