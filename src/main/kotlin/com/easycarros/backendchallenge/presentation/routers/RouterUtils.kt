package com.easycarros.backendchallenge.presentation.routers

import com.easycarros.backendchallenge.business.CredentialService
import com.easycarros.backendchallenge.common.Auditor
import com.easycarros.backendchallenge.serializers.SerializerBuilder
import io.vertx.core.json.JsonObject
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.core.http.HttpServer
import javax.inject.Inject
import javax.inject.Named

class RouterUtils @Inject constructor(internal val vertx: Vertx,
                                      internal val server: HttpServer,
                                      @Named("presentation") internal val auditor: Auditor,
                                      @Named("config") internal val config: JsonObject,
                                      internal val credentialService: com.easycarros.backendchallenge.business.CredentialService,
                                      internal val serializerBuilder: SerializerBuilder)
