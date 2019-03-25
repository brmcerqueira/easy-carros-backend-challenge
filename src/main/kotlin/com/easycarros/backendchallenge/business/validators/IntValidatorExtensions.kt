package com.easycarros.backendchallenge.business.validators

fun <T> ValidatorType<T, Int>.max(value: Int) = this.add {
    if (it.value > value) {
        it.error("max")
    }
}
