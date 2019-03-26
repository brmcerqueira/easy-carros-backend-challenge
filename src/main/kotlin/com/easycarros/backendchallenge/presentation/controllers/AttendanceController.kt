package com.easycarros.backendchallenge.presentation.controllers

import com.easycarros.backendchallenge.business.AttendanceService
import com.easycarros.backendchallenge.dto.RequestDto
import com.easycarros.backendchallenge.presentation.body
import com.easycarros.backendchallenge.presentation.json
import com.easycarros.backendchallenge.presentation.routers.RouteResult
import io.reactivex.Single
import io.vertx.reactivex.ext.web.RoutingContext
import javax.inject.Inject

class AttendanceController @Inject constructor(private val service: AttendanceService) {
    fun request(input: Single<RoutingContext>): Single<RouteResult> =
            input.body<RequestDto>().flatMap(service::request).map(::json)

    fun search(input: Single<RoutingContext>): Single<RouteResult> = input.flatMap {
        service.search(it.queryParam("address").first())
    }.map(::json)
}