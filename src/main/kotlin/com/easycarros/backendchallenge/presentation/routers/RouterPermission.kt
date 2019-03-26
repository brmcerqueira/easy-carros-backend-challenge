package com.easycarros.backendchallenge.presentation.routers

class RouterPermission {
    var onlyAuthenticated = false

    fun copy(): RouterPermission {
        val copyRouterPermission = RouterPermission()
        copyRouterPermission.onlyAuthenticated = onlyAuthenticated
        return copyRouterPermission
    }
}
