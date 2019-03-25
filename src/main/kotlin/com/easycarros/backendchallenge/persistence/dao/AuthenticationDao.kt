package com.easycarros.backendchallenge.persistence.dao

import com.brmcerqueira.kuerongo.Pipeline
import com.easycarros.backendchallenge.dto.output.CredentialOutputDto
import com.easycarros.backendchallenge.persistence.Database
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class AuthenticationDao @Inject constructor(private val database: Database) {
    fun getCredentialByEmail(email: String): Single<Optional<CredentialOutputDto>> = database.genericAggregateOne("credential", Pipeline {
        match {
            "email" *= email
            "enabled" *= true
        }

        project {
            +"password"
        }
    })
}
