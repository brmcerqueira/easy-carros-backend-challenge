package com.easycarros.backendchallenge.business.validators

import com.easycarros.backendchallenge.common.Translate
import java.util.*

interface IValidatorCheck<T> {
    fun validate(entity: T, translate: Translate, errors: HashSet<String>)
}
