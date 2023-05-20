package com.jms.a20220602_navermap.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GeoInfo(
    @field:Json(name = "addresses")
    val addresses: List<Addresse>?,
    @field:Json(name = "errorMessage")
    val errorMessage: String?,
    @field:Json(name = "meta")
    val meta: Meta?,
    @field:Json(name = "status")
    val status: String?
)