package com.jms.searchpharmacy.data.model.reversegeo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Code(
    @Json(name = "id")
    val id: String?,
    @Json(name = "mappingId")
    val mappingId: String?,
    @Json(name = "type")
    val type: String?
)