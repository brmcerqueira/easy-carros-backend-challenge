package com.easycarros.backendchallenge.presentation.routers

import com.easycarros.backendchallenge.presentation.PresentationComponent
import io.vertx.reactivex.ext.web.Router

class RouterBranch(presentationComponent: PresentationComponent,
                   router: Router,
                   routerPermission: RouterPermission,
                   routerUtils: RouterUtils)
    : AbstractRouter(presentationComponent, router, routerPermission, routerUtils) {


    fun onlyAuthenticated(): RouterBranch {
        routerPermission.onlyAuthenticated = true
        return this
    }

    infix fun to(action: RouterRoot.() -> Unit) {
        RouterRoot(presentationComponent, router, routerPermission.copy(), routerUtils).action()
    }
}
