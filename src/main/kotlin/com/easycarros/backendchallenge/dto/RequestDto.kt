package com.easycarros.backendchallenge.dto

import com.easycarros.backendchallenge.domain.ServiceKind

data class RequestDto(val kind: ServiceKind, val lat: Double, val lng: Double)