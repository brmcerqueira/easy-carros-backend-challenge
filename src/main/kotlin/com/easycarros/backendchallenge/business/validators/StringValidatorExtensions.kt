package com.easycarros.backendchallenge.business.validators

fun <T> ValidatorType<T, String>.notEmpty() = this.add {
    if (it.value.isEmpty()) {
        it.error("not_empty")
    }
}

fun <T> ValidatorType<T, String>.min(value: Int) = this.add {
    if (it.value.length < value) {
        it.error("min")
    }
}

fun <T> ValidatorType<T, String>.max(value: Int) = this.add {
    if (it.value.length > value) {
        it.error("max")
    }
}
