package com.jms.searchpharmacy.data.model.server

import com.google.gson.annotations.SerializedName

class Pharmacy(
    @field:SerializedName("loadaddress")
    var load_address: String,

    @field:SerializedName(
        "name"
    ) var name: String,

    @field:SerializedName("address")
    var address: String,

    @field:SerializedName(
        "startdate"
    ) var startdate: String
)