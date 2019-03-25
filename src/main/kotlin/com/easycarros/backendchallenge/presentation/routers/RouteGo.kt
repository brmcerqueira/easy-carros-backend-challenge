package com.easycarros.backendchallenge.presentation.routers

import com.easycarros.backendchallenge.common.Translate
import com.easycarros.backendchallenge.exceptions.I18nException
import com.easycarros.backendchallenge.exceptions.NotAuthorizedException
import com.easycarros.backendchallenge.exceptions.ValidatorException
import com.easycarros.backendchallenge.presentation.PresentationComponent
import com.easycarros.backendchallenge.presentation.RequestComponent
import com.easycarros.backendchallenge.presentation.RequestModule
import io.reactivex.Single
import io.vertx.core.json.Json
import io.vertx.reactivex.ext.web.Route
import io.vertx.reactivex.ext.web.RoutingContext
import java.util.*

class RouteGo(private val presentationComponent: PresentationComponent,
        private val routerPermission: RouterPermission,
        private val routerUtils: RouterUtils,
        private val route: Route) {

    private val authorization = "Authorization"

    private data class Message(val message: String, val show: Boolean = true)
    private data class Error(val message: String, val errors: HashSet<String>)

    infix fun go(action: RequestComponent.() -> (r: Single<RoutingContext>) -> Single<RouteResult>) {
        route.handler { request ->
            try {
                request.put("auditor", routerUtils.auditor)
                val requestModule = RequestModule(request)
                val requestComponent = presentationComponent.requestComponent(requestModule)
                check(request, requestModule).flatMap { requestComponent.action().invoke(Single.just(it)) }.subscribe({
                    if (it.chunk != null) {
                        end(request, it.chunk)
                    }
                    else  {
                        routerUtils.auditor.info("route-result -> ok")
                        request.response().end()
                    }
                }, {
                    end(request, it, requestComponent.translate)
                })
            }
            catch (ex: Exception) {
                end(request, ex)
            }
        }

        route.failureHandler { request ->
            end(request, request.failure())
        }
    }

    private fun check(routingContext: RoutingContext, requestModule: RequestModule): Single<RoutingContext> {
        if (routerPermission.onlyAuthenticated || routerPermission.kinds.isNotEmpty() || routerPermission.permissions.isNotEmpty()) {
            val headers = routingContext.request().headers()
            return if (headers.contains(authorization)) {
                routerUtils.credentialService.check(headers.get(authorization),
                    routerPermission.kinds,
                    routerPermission.permissions).map {
                    requestModule.credential = it
                    routingContext
                }.onErrorResumeNext {
                    Single.error(NotAuthorizedException())
                }
            } else {
                Single.error(NotAuthorizedException())
            }
        }

        return Single.just(routingContext)
    }

    private fun end(request: RoutingContext, throwable: Throwable, translate: Translate? = null) {
        routerUtils.auditor.throwable("failure", throwable)
        val response = request.response()
        response.statusCode = 500
        end(request, when (throwable) {
            is NotAuthorizedException -> {
                response.statusCode = 401
                Message(translate?.get("not-authorized") ?: "not-authorized")
            }
            is ValidatorException -> Error(translate?.get(throwable.message.orEmpty()) ?: throwable.message.orEmpty(), throwable.errors)
            is I18nException -> Message(translate?.get(throwable.message.orEmpty(), *throwable.formatArgs().orEmpty()) ?: throwable.message.orEmpty())
            else -> Message(throwable.message.orEmpty())
        })
    }

    private fun end(request: RoutingContext, chunk: Any) {
        routerUtils.auditor.info("route-result -> {0}", chunk)
        request.response().putHeader("content-type", "text/json; charset=UTF-8").end(Json.encode(chunk))
    }
}
