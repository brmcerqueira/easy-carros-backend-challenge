package com.easycarros.backendchallenge.business.validators

import io.reactivex.Single

fun <T> Single<T>.validate(validator: Validator<T>): Single<T> = this.map {
    validator.validate(it)
    it
}
