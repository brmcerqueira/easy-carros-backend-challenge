package com.easycarros.backendchallenge.persistence.dao

import com.easycarros.backendchallenge.domain.Partner
import com.easycarros.backendchallenge.dto.RequestDto
import com.easycarros.backendchallenge.exceptions.PartnerNotFoundException
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
                centerSphere(dto.lng, dto.lat, 10.0.km())
            }
        }
    }).map {
        if (it.isPresent) it.get() else throw PartnerNotFoundException()
    }

    fun search(lng: Double, lat: Double) = database.genericFind<Partner>("partner", Find {
        "location" *= {
            geoWithin {
                centerSphere(lng, lat, 10.0.km())
            }
        }
    })

    /**
     * Função para converter kilometros em radius.
     */
    private fun Double.km(): Double = this / 6378.1
}