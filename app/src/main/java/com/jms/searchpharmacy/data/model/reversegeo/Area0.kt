package com.jms.searchpharmacy.data.model.reversegeo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Area0(
    @Json(name = "coords")
    val coords: Coords?,
    @Json(name = "name")
    val name: String?
)