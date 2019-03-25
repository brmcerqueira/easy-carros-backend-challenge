package com.easycarros.backendchallenge.serializers

import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import kotlin.reflect.KClass

fun <T : Any> ObjectMapper.mapTo(jsonObject: JsonObject, kclass: KClass<T>) = this.convertValue(jsonObject.map, kclass.java)

fun <T : Any> ObjectMapper.mapTo(jsonArray: JsonArray, kclass: KClass<T>) = this.convertValue(jsonArray.list, kclass.java)

@Suppress("UNCHECKED_CAST")
fun ObjectMapper.mapFrom(obj: Any) = JsonObject(this.convertValue(obj, Map::class.java) as Map<String, Any>)

inline fun <reified T> SimpleModule.registerSerializer(jsonSerializer: JsonSerializer<T>) {
    this.addSerializer(T::class.java, jsonSerializer)
}

inline fun <reified T> SimpleModule.registerDeserializer(jsonDeserializer: JsonDeserializer<T>) {
    this.addDeserializer(T::class.java, jsonDeserializer)
}

inline fun <reified T: Enum<T>> SimpleModule.registerEnumSerializer() {
    this.addSerializer(T::class.java, EnumSerializer())
}

inline fun <reified T: Enum<T>> SimpleModule.registerEnumDeserializer() {
    this.addDeserializer(T::class.java, EnumDeserializer(enumValues<T>()))
}