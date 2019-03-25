package com.easycarros.backendchallenge.domain

import com.easycarros.backendchallenge.serializers.Sensitive
import java.time.LocalDate

data class Credential(
    val _id: String?,
    val name: String,
    val email: String,
    val enabled: Boolean,
    @field:Sensitive val password: String
)