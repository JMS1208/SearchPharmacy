package com.jms.searchpharmacy.data.model.reversegeo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Status(
    @Json(name = "code")
    val code: Int?,
    @Json(name = "message")
    val message: String?,
    @Json(name = "name")
    val name: String?
)