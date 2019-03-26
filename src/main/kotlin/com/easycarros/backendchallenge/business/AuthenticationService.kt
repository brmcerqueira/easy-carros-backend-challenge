package com.easycarros.backendchallenge.business

import com.brmcerqueira.kuerongo.Json
import com.easycarros.backendchallenge.business.validators.ValidatorBuilder
import com.easycarros.backendchallenge.business.validators.notEmpty
import com.easycarros.backendchallenge.common.Auditor
import com.easycarros.backendchallenge.dto.SignInDto
import com.easycarros.backendchallenge.exceptions.SignInException
import com.easycarros.backendchallenge.persistence.dao.AuthenticationDao
import io.reactivex.Single
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.ext.auth.jwt.JWTAuth
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject
import javax.inject.Named

class AuthenticationService @Inject constructor(validatorBuilder: ValidatorBuilder,
                                                private val auth: JWTAuth,
                                                @Named("business") private val auditor: Auditor,
                                                private val dao: AuthenticationDao) {

    private val validatorSignInDto = validatorBuilder.create<SignInDto> {
        let { email } check {
            withName("email")
            notEmpty()
        }
        let { password } check {
            withName("password")
            notEmpty()
        }
    }

    fun signIn(dto: SignInDto): Single<String> {
        validatorSignInDto.validate(dto)
        return dao.getCredentialByEmail(dto.email).map {
            if (!it.isPresent || dto.password != it.get().password) {
                throw SignInException()
            }

            val now = LocalDateTime.now()
            val json = Json {
                //sub (subject) = Entidade à quem o token pertence, normalmente o ID do usuário;
                "sub" *= it.get()._id
                //iss (issuer) = Emissor do token;
                "iss" *= "easycarros"
                //iat (issued at) = Timestamp de quando o token foi criado;
                "iat" *= now.atZone(ZoneId.systemDefault()).toEpochSecond()
                //exp (expiration) = Timestamp de quando o token irá expirar;
                "exp" *= now.plusDays(3).atZone(ZoneId.systemDefault()).toEpochSecond()
                //aud (audience) = Destinatário do token, representa a aplicação que irá usá-lo.
                "aud" *= "front-end"
                "permissions" *= arrayOf<String>()
            }.raw<JsonObject>()

            auditor.info("jwt-payload -> {0}", json)

            auth.generateToken(json)
        }
    }
}