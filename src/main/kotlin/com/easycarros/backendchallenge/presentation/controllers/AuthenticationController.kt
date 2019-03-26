package com.easycarros.backendchallenge.presentation.controllers

import com.easycarros.backendchallenge.business.AuthenticationService
import com.easycarros.backendchallenge.dto.SignInDto
import com.easycarros.backendchallenge.presentation.body
import com.easycarros.backendchallenge.presentation.json
import com.easycarros.backendchallenge.presentation.routers.RouteResult
import io.reactivex.Single
import io.vertx.reactivex.ext.web.RoutingContext
import javax.inject.Inject

class AuthenticationController @Inject constructor(private val service: AuthenticationService) {
    fun signIn(input: Single<RoutingContext>): Single<RouteResult> =
            input.body<SignInDto>().flatMap(service::signIn).map(::json)
}