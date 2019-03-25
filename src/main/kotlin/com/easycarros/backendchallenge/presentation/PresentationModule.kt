package com.easycarros.backendchallenge.presentation

import com.easycarros.backendchallenge.common.Auditor
import dagger.Module
import dagger.Provides
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.ext.auth.KeyStoreOptions
import io.vertx.kotlin.ext.auth.jwt.JWTAuthOptions
import io.vertx.reactivex.core.Vertx
import io.vertx.reactivex.ext.auth.jwt.JWTAuth
import javax.inject.Named
import javax.inject.Singleton

@Module
class PresentationModule constructor(private val vertx: Vertx, private val config: () -> JsonObject) {
    @Provides
    @Singleton
    fun provideVertx() = vertx

    @Provides
    @Singleton
    @Named("presentation")
    fun provideAuditor() = Auditor("presentation")

    @Provides
    @Named("config")
    fun provideConfig() = config()

    @Provides
    @Singleton
    fun provideHttpServer() = vertx.createHttpServer()

    @Provides
    @Singleton
    fun provideJWTAuth(@Named("config") config: JsonObject): JWTAuth {
        val auth = config.getJsonObject("auth")
        return JWTAuth.create(vertx, JWTAuthOptions(
            permissionsClaimKey = "permissions",
            keyStore = KeyStoreOptions( path = auth.getString("path"),
            password = auth.getString("password"))))
    }
}
