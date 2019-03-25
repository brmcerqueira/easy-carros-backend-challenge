package com.easycarros.backendchallenge.business.validators

import com.easycarros.backendchallenge.common.Translate
import javax.inject.Inject

class ValidatorBuilder @Inject
    constructor(private val translate: Translate) {

    fun <T> create(init: Validator<T>.() -> Unit): Validator<T> {
        val validator = Validator<T>(translate)
        validator.init()
        return validator
    }
}
