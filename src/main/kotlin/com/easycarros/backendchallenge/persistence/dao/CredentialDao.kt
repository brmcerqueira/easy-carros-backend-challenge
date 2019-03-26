package com.easycarros.backendchallenge.persistence.dao

import com.easycarros.backendchallenge.domain.Credential
import com.easycarros.backendchallenge.persistence.Database
import com.easycarros.backendchallenge.persistence.Find
import io.vertx.core.json.JsonObject
import javax.inject.Inject

class CredentialDao @Inject constructor(private val database: Database) {
    fun getCredentialByPrincipal(principal: JsonObject) = database.genericFindOne<Credential>("credential", Find {
        "_id" *= principal.getString("sub")
    }).map {
        it.orElseThrow()
    }
}