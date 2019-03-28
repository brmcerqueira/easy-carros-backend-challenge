package com.easycarros.backendchallenge.consumers

import com.easycarros.backendchallenge.dto.output.LocationOutputDto
import io.reactivex.Single
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class GeocodeConsumer @Inject constructor(@Named("geocode") private val consume: ConsumeApi){
    private data class GeocodeOutputDto(
        val longt: Double,
        val latt: Double,
        val error: ErrorOutputDto?
    )

    private data class ErrorOutputDto(
        val description: String,
        val code: String
    )

    fun geocode(address: String): Single<Optional<LocationOutputDto>> =
            consume.get<GeocodeOutputDto>("/${URLEncoder.encode(address, "UTF-8")}?json=1&auth=${consume.options!!.getString("auth")}").map {
        if (it.error == null) Optional.of(LocationOutputDto(
            lat =  it.latt,
            lng = it.longt
        )) else Optional.empty()
    }.onErrorResumeNext {
        Single.just(Optional.empty())
    }
}