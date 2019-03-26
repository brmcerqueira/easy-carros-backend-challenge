package com.easycarros.backendchallenge.business

import com.easycarros.backendchallenge.domain.Partner
import com.easycarros.backendchallenge.dto.RequestDto
import com.easycarros.backendchallenge.persistence.dao.AttendanceDao
import io.reactivex.Single
import javax.inject.Inject

class AttendanceService @Inject constructor(private val dao: AttendanceDao) {
    fun request(dto: RequestDto): Single<Partner> = dao.request(dto)
}