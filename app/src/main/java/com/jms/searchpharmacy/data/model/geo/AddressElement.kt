package com.jms.a20220602_navermap.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AddressElement(
    @field:Json(name = "code")
    val code: String?,
    @field:Json(name = "longName")
    val longName: String?,
    @field:Json(name = "shortName")
    val shortName: String?,
    @field:Json(name = "types")
    val types: List<String>?
)