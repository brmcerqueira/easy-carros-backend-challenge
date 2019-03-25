package com.easycarros.backendchallenge.business.validators

import com.easycarros.backendchallenge.common.Translate
import com.easycarros.backendchallenge.exceptions.ValidatorException
import java.util.*

@ValidatorMarker
class Validator<T> constructor(private val translate: Translate) {
    private val checks = HashSet<com.easycarros.backendchallenge.business.validators.IValidatorCheck<T>>()

    fun validate(entity: T) {
        val errors = HashSet<String>()

        for (item in checks) {
            item.validate(entity, translate, errors)
        }

        if (errors.any()) {
            throw ValidatorException(errors)
        }
    }

    private fun <TValidatorCheck : com.easycarros.backendchallenge.business.validators.IValidatorCheck<T>> add(validatorCheck: TValidatorCheck): TValidatorCheck {
        checks.add(validatorCheck)
        return validatorCheck
    }

    fun <TResult> let(action: T.() -> TResult) = add(ValidatorCheck(ValidatorType(action)))
}
