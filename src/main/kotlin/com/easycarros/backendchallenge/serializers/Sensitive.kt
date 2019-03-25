package com.easycarros.backendchallenge.serializers

import com.fasterxml.jackson.annotation.JacksonAnnotation

@Target(AnnotationTarget.FIELD)
@Retention
@JacksonAnnotation
annotation class Sensitive(val value: String = "", val securePersistence: Boolean = false)