package com.easycarros.backendchallenge.dto

import com.easycarros.backendchallenge.serializers.Sensitive

data class SignInDto(val email: String, @field:Sensitive val password: String)