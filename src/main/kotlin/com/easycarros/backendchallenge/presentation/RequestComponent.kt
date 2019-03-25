package com.easycarros.backendchallenge.presentation

import com.easycarros.backendchallenge.common.Translate
import com.easycarros.backendchallenge.presentation.controllers.*
import dagger.Subcomponent

@RequestScope
@Subcomponent(modules = [(RequestModule::class)])
interface RequestComponent {
    val translate: Translate
    val authenticationController: AuthenticationController
}
