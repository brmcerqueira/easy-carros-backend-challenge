package com.easycarros.backendchallenge.presentation

import com.easycarros.backendchallenge.common.Auditor
import com.easycarros.backendchallenge.presentation.routers.RouteResult
import io.reactivex.Single
import io.vertx.core.json.Json
import io.vertx.reactivex.ext.web.RoutingContext
import kotlin.reflect.KClass

fun ok(): RouteResult = RouteResult()

fun <T> json(data: T): RouteResult = RouteResult(data)

fun <T: Any> Single<RoutingContext>.body(kclass: KClass<T>): Single<T> = this.map {
    val result = it.bodyAsJson.mapTo(kclass.java)
    it.get<Auditor>("auditor").info("body -> {0}", result)
    result
}

fun <T: Any> Single<RoutingContext>.bodyCollection(kclass: KClass<T>): Single<List<T>> = this.map {
    val result = it.bodyAsJsonArray.map { Json.mapper.convertValue(it, kclass.java) }
    it.get<Auditor>("auditor").info("body -> {0}", result)
    result
}

inline fun <reified T: Any> Single<RoutingContext>.body(): Single<T> = this.body(T::class)

inline fun <reified T: Any> Single<RoutingContext>.bodyCollection(): Single<List<T>> = this.bodyCollection(T::class)
