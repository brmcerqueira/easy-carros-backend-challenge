package com.easycarros.backendchallenge.presentation.routers

import com.easycarros.backendchallenge.presentation.PresentationComponent
import com.easycarros.backendchallenge.serializers.SerializerBuilder
import io.vertx.reactivex.ext.web.Router as VertxRouter
import io.vertx.reactivex.ext.web.handler.BodyHandler
import io.vertx.reactivex.ext.web.handler.StaticHandler
import javax.inject.Inject

@RouterMarker
class RouterBuilder @Inject
    constructor(private val presentationComponent: PresentationComponent, private val routerUtils: RouterUtils) {

    fun serializer(action: SerializerBuilder.() -> Unit): RouterBuilder {
        routerUtils.serializerBuilder.action()
        return this
    }

    fun build(init: RouterRoot.() -> Unit) {
        routerUtils.serializerBuilder.build()
        val vertxRouter = VertxRouter.router(routerUtils.vertx)
        vertxRouter.route().handler(BodyHandler.create())
        val router = RouterRoot(presentationComponent, vertxRouter, routerUtils)
        router.init()
        vertxRouter.route().handler(StaticHandler.create())
        routerUtils.server.requestHandler { vertxRouter.accept(it) }
        routerUtils.auditor.info("environment: {0}", routerUtils.config.getString("environment"))
        routerUtils.server.rxListen(routerUtils.config.getInteger("port", 8080)).subscribe({
            routerUtils.auditor.info("listen: {0}", it.actualPort().toString())
        }, {
            routerUtils.auditor.info("failure", it)
            routerUtils.server.close()
        })
    }
}
