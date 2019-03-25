package com.easycarros.backendchallenge.common

import io.vertx.core.json.Json
import io.vertx.core.logging.LoggerFactory

class Auditor constructor(private val name: String) {

    private val logger = LoggerFactory.getLogger(name)

    fun info(message: String, vararg objects: Any) {
        logger.info(message, *objects.treat())
    }

    fun throwable(message: String, throwable: Throwable, vararg objects: Any) {
        logger.info(message, throwable, *objects.treat())
    }

    private fun Array<out Any>.treat(): Array<Any> {
        return this.map {
            when(it) {
                is String -> it
                else -> Json.prettyMapper.writeValueAsString(it)
            }
        }.toTypedArray()
    }
}