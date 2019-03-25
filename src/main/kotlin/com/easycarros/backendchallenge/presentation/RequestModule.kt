package com.easycarros.backendchallenge.presentation

import com.easycarros.backendchallenge.common.Translate
import com.easycarros.backendchallenge.domain.Credential
import dagger.Module
import dagger.Provides
import java.util.*
import io.vertx.reactivex.ext.web.RoutingContext

@Module
class RequestModule constructor(private val routingContext: RoutingContext) {

    var credential: Credential? = null

    @Provides
    @RequestScope
    fun provideTranslate(): Translate {
        val resourceBundle = try {
            val preferredLocale = routingContext.preferredLocale()
            ResourceBundle.getBundle("messages", java.util.Locale(preferredLocale.language(),
                if(preferredLocale.country() != null) preferredLocale.country() else "",
                if(preferredLocale.variant() != null) preferredLocale.variant() else ""))
        }
        catch (ex: Exception) {
            ResourceBundle.getBundle("messages")
        }
        return Translate(resourceBundle)
    }

    @Provides
    @RequestScope
    fun provideCredential() = credential
}
