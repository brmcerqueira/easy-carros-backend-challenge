package com.easycarros.backendchallenge.domain

data class Partner(
    val _id: String?,
    val name: String,
    val location: Location,
    val availableServices: Array<ServiceKind>
)