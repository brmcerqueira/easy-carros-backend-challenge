package com.easycarros.backendchallenge.persistence

import com.brmcerqueira.kuerongo.Expression
import com.brmcerqueira.kuerongo.Project
import com.brmcerqueira.kuerongo.Sort
import io.vertx.core.json.JsonObject
import io.vertx.ext.mongo.FindOptions

class Find() : Expression() {
    val options = FindOptions()

    constructor(init: Find.() -> Unit) : this() {
        init()
    }

    fun limit(value: Int) {
        options.limit = value
    }

    fun skip(value: Int) {
        options.skip = value
    }

    infix fun project(init: Project.() -> Unit) {
        val project = Project()
        project.init()
        val raw = project.raw<JsonObject>()
        if (!raw.isEmpty) {
            options.fields = raw
        }
    }

    infix fun sort(init: Sort.() -> Unit) {
        val sort = Sort()
        sort.init()
        val raw = sort.raw<JsonObject>()
        if (!raw.isEmpty) {
            options.sort = raw
        }
    }

    infix fun text(value: String?) {
        if (value != null) {
            raw<JsonObject>().put("\$text", JsonObject().put("\$search", value))
        }
    }
}
