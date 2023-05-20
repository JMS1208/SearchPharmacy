package com.jms.searchpharmacy.data.model.reversegeo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ReverseGeoInfo(
    @Json(name = "results")
    val results: List<Result>?,
    @Json(name = "status")
    val status: Status?
)