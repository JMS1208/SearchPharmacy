package com.jms.searchpharmacy.data.model.reversegeo


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Region(
    @Json(name = "area0")
    val area0: Area0?,
    @Json(name = "area1")
    val area1: Area1?,
    @Json(name = "area2")
    val area2: Area2?,
    @Json(name = "area3")
    val area3: Area3?,
    @Json(name = "area4")
    val area4: Area4?
)