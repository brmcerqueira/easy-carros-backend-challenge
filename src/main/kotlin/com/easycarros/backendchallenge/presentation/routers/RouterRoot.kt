package com.easycarros.backendchallenge.presentation.routers

import com.easycarros.backendchallenge.presentation.PresentationComponent
import io.vertx.reactivex.ext.web.Router
import io.vertx.reactivex.ext.web.RoutingContext
import io.vertx.core.Handler

class RouterRoot(
        presentationComponent: PresentationComponent,
        router: Router,
        routerPermission: RouterPermission,
        routerUtils: RouterUtils)
    : AbstractRouter(presentationComponent, router, routerPermission, routerUtils) {

    constructor(presentationComponent: PresentationComponent, router: Router, routerUtils: RouterUtils) :
        this(presentationComponent, router, RouterPermission(), routerUtils)


    fun onlyAuthenticated(): RouterBranch {
        val newRouterAuthentication = routerPermission.copy()
        newRouterAuthentication.onlyAuthenticated = true
        return RouterBranch(presentationComponent, router, newRouterAuthentication, routerUtils)
    }

    fun handler(path: String, requestHandler: Handler<RoutingContext>) {
        router.route(path).handler(requestHandler)
    }
}

