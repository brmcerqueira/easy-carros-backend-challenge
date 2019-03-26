package com.easycarros.backendchallenge.business

import com.easycarros.backendchallenge.consumers.GeocodeConsumer
import com.easycarros.backendchallenge.domain.Partner
import com.easycarros.backendchallenge.dto.RequestDto
import com.easycarros.backendchallenge.persistence.dao.AttendanceDao
import io.reactivex.Single
import javax.inject.Inject

class AttendanceService @Inject constructor(private val dao: AttendanceDao, private val consumer: GeocodeConsumer) {
    fun request(dto: RequestDto): Single<Partner> = dao.request(dto)
    fun search(address: String): Single<List<Partner>> = consumer.geocode(address).flatMap {
        if (it.isPresent) dao.search(it.get().lng, it.get().lat) else Single.just(listOf())
    }
}