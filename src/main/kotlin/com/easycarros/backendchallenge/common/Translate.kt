package com.easycarros.backendchallenge.common

import java.text.MessageFormat
import java.util.*

class Translate constructor(private val resourceBundle: ResourceBundle) {
    fun get(key: String, vararg arguments: Any?): String {
        return try {
            val value = resourceBundle.getString(key)
            if (arguments.isNotEmpty()) MessageFormat.format(value, *arguments) else value
        }
        catch (ex: MissingResourceException) {
            key
        }
    }
}
