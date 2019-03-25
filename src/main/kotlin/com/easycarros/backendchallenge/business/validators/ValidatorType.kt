package com.easycarros.backendchallenge.business.validators

import com.easycarros.backendchallenge.common.Translate
import java.util.*

@ValidatorMarker
class ValidatorType<T, TType: Any?> constructor(private val getValue: T.() -> TType) {

    private val checks = HashSet<(context: ValidatorContext<TType>) -> Unit>()

    private var name: String = ""

    fun withName(name: String) {
        this.name = name
    }

    fun validate(entity: T, translate: Translate, errors: HashSet<String>) {
        val context = ValidatorContext(name, entity.getValue(), translate, errors)
        for (item in checks) {
            item(context)
        }
    }

    fun notNull() {
        checks.add {
            if (it.value == null) {
                it.error("not_null")
            }
        }
    }

    fun add(action: (context: ValidatorContext<TType>) -> Unit) = checks.add(action)
}
