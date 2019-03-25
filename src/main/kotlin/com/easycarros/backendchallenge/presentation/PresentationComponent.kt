package com.easycarros.backendchallenge.presentation

import com.easycarros.backendchallenge.business.BusinessModule
import com.easycarros.backendchallenge.consumers.ConsumeModule
import com.easycarros.backendchallenge.persistence.PersistenceModule
import com.easycarros.backendchallenge.presentation.routers.RouterBuilder
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [(PresentationModule::class),
    (ConsumeModule::class),
    (com.easycarros.backendchallenge.business.BusinessModule::class),
    (PersistenceModule::class)])
interface PresentationComponent {
    val routerBuilder: RouterBuilder
    fun requestComponent(requestModule: RequestModule): RequestComponent
}