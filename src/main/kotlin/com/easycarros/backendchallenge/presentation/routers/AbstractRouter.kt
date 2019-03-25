package com.easycarros.backendchallenge.presentation.routers

import com.easycarros.backendchallenge.presentation.PresentationComponent
import io.vertx.reactivex.ext.web.Router

abstract class AbstractRouter(protected val presentationComponent: PresentationComponent,
                              protected val router: Router,
                              protected val routerPermission: RouterPermission,
                              protected val routerUtils: RouterUtils) {
    private fun resolve(path: String): String = "/api/$path"
    fun route(path: String? = null): RouteGo = RouteGo(presentationComponent, routerPermission, routerUtils,
        if (path != null) router.route(resolve(path)) else router.route())
    fun get(path: String? = null): RouteGo = RouteGo(presentationComponent, routerPermission, routerUtils,
        if (path != null) router.get(resolve(path)) else router.get())
    fun post(path: String? = null): RouteGo = RouteGo(presentationComponent, routerPermission, routerUtils,
        if (path != null) router.post(resolve(path)) else router.post())
    fun put(path: String? = null): RouteGo = RouteGo(presentationComponent, routerPermission, routerUtils,
        if (path != null) router.put(resolve(path)) else router.put())
    fun delete(path: String? = null): RouteGo = RouteGo(presentationComponent, routerPermission, routerUtils,
        if (path != null) router.delete(resolve(path)) else router.delete())
}
