package com.jms.a20220602_navermap.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Addresse(
    @field:Json(name = "addressElements")
    val addressElements: List<AddressElement>?,
    @field:Json(name = "distance")
    val distance: Double?,
    @field:Json(name = "englishAddress")
    val englishAddress: String?,
    @field:Json(name = "jibunAddress")
    val jibunAddress: String?,
    @field:Json(name = "roadAddress")
    val roadAddress: String?,
    @field:Json(name = "x")
    val x: String?,
    @field:Json(name = "y")
    val y: String?
)