package com.jms.searchpharmacy.data.model.reversegeo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Area3(
    @Json(name = "coords")
    val coords: CoordsXXX?,
    @Json(name = "name")
    val name: String?
)