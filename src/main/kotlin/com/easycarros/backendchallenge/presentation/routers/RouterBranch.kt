package com.easycarros.backendchallenge.presentation.routers

import com.easycarros.backendchallenge.domain.CredentialKind
import com.easycarros.backendchallenge.domain.Permission
import com.easycarros.backendchallenge.presentation.PresentationComponent
import io.vertx.reactivex.ext.web.Router

class RouterBranch(presentationComponent: PresentationComponent,
                   router: Router,
                   routerPermission: RouterPermission,
                   routerUtils: RouterUtils)
    : AbstractRouter(presentationComponent, router, routerPermission, routerUtils) {

    fun allow(vararg kinds: CredentialKind): RouterBranch {
        kinds.toCollection(routerPermission.kinds)
        return this
    }

    fun allow(vararg permissions: Permission): RouterBranch {
        permissions.toCollection(routerPermission.permissions)
        return this
    }

    fun onlyAuthenticated(): RouterBranch {
        routerPermission.onlyAuthenticated = true
        return this
    }

    infix fun to(action: RouterRoot.() -> Unit) {
        RouterRoot(presentationComponent, router, routerPermission.copy(), routerUtils).action()
    }
}
