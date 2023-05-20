package com.jms.searchpharmacy.data.model.reversegeo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Center(
    @Json(name = "crs")
    val crs: String?,
    @Json(name = "x")
    val x: Double?,
    @Json(name = "y")
    val y: Double?
)