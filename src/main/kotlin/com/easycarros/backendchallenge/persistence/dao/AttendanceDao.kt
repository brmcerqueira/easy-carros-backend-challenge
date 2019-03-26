package com.easycarros.backendchallenge.persistence.dao

import com.easycarros.backendchallenge.domain.Partner
import com.easycarros.backendchallenge.dto.RequestDto
import com.easycarros.backendchallenge.persistence.Database
import com.easycarros.backendchallenge.persistence.Find
import javax.inject.Inject

class AttendanceDao @Inject constructor(private val database: Database) {
    fun request(dto: RequestDto) = database.genericFindOne<Partner>("partner", Find {
        "availableServices" *= {
            into(listOf(dto.kind))
        }
        "location" *= {
            geoWithin {
                centerSphere(dto.lng, dto.lat, 5 / 3963.2)
            }
        }
    }).map {
        it.orElseThrow()
    }
}