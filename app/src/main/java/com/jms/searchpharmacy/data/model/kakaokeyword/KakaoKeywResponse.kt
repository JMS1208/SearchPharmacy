package com.jms.searchpharmacy.data.model.kakaokeyword


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class KakaoKeywResponse(
    @Json(name = "documents")
    val documents: List<Document>?,
    @Json(name = "meta")
    val meta: Meta?
)