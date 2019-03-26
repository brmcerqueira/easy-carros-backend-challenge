package com.easycarros.backendchallenge.consumers

import com.easycarros.backendchallenge.dto.output.LocationOutputDto
import io.reactivex.Single
import java.net.URLEncoder
import java.util.*
import javax.inject.Inject
import javax.inject.Named

class GeocodeConsumer @Inject constructor(@Named("googleapis") private val consume: ConsumeApi){
    private data class GeocodeOutputDto(
        val results: Array<ResultOutputDto>
    )

    private data class ResultOutputDto(
        val geometry: GeometryOutputDto
    )

    private data class GeometryOutputDto(
        val location: LocationOutputDto
    )

    fun geocode(address: String): Single<Optional<LocationOutputDto>> =
            consume.get<GeocodeOutputDto>("/maps/api/geocode/json?address=$address&key=${URLEncoder.encode(consume.options!!.getString("key"), "UTF-8")}").map {
        if (it.results.isNotEmpty()) Optional.of(it.results.first().geometry.location)
        Optional.empty<LocationOutputDto>()
    }
}