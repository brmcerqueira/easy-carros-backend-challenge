package com.easycarros.backendchallenge.presentation

import com.easycarros.backendchallenge.domain.ServiceKind
import com.easycarros.backendchallenge.serializers.registerEnumDeserializer
import com.easycarros.backendchallenge.serializers.registerEnumSerializer
import io.vertx.reactivex.core.AbstractVerticle

class MainVerticle : AbstractVerticle() {
    override fun start() {
        val presentation = DaggerPresentationComponent.builder()
            .presentationModule(PresentationModule(vertx, ::config))
            .build()

        presentation.routerBuilder.serializer {
            all {
                registerEnumSerializer<ServiceKind>()
                registerEnumDeserializer<ServiceKind>()
            }
        }.build {
            post("sign/in") go { authenticationController::signIn }
            onlyAuthenticated() to {
                post("attendance/request") go { attendanceController::request }
                get("attendance/search") go { attendanceController::search }
            }
        }
    }
}
