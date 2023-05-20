package com.jms.searchpharmacy.data.model.reversegeo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Result(
    @Json(name = "code")
    val code: Code?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "region")
    val region: Region?
)