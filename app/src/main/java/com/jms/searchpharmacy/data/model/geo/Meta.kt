package com.jms.a20220602_navermap.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Meta(
    @field:Json(name = "count")
    val count: Int?,
    @field:Json(name = "page")
    val page: Int?,
    @field:Json(name = "totalCount")
    val totalCount: Int?
)