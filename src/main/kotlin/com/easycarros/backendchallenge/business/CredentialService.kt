package com.easycarros.backendchallenge.business

import com.brmcerqueira.kuerongo.Json
import com.easycarros.backendchallenge.domain.Credential
import com.easycarros.backendchallenge.persistence.dao.CredentialDao
import io.reactivex.Single
import io.vertx.reactivex.ext.auth.jwt.JWTAuth
import javax.inject.Inject

class CredentialService @Inject constructor(private val auth: JWTAuth, private val dao: CredentialDao) {
    fun check(authorization: String): Single<Credential> {
        return auth.rxAuthenticate(Json {
            "jwt" *= authorization
            "options" *= {
                "ignoreExpiration" *= true
            }
        }.raw()).flatMap {
            dao.getCredentialByPrincipal(it.principal())
        }
    }
}
