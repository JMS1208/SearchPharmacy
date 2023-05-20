package com.jms.searchpharmacy.data.model.server

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "pharLocation")
@kotlinx.parcelize.Parcelize
data class PharmacyLocation(
    @field:SerializedName("index")
    @PrimaryKey
    var index: Int, //    load_address -> loadaddress

    @field:SerializedName("loadaddress")
    var load_address: String,

    @field:SerializedName("hospitalcount")
    var hospital_count: Int,

    @field:SerializedName("pharmacycount")
    var pharmacy_count: Int,

    @field:SerializedName("conveniencecount")
    var convenience_count: Int, //    추가

    @field:SerializedName("doctorcount")
    var doctorcount: Int,

    @field:SerializedName("dong")
    var dong: String,

    @field:SerializedName("hospitalperpharmacy")
    var hospital_per_pharmacy: Float,

    @field:SerializedName("doctorperpharmacy")
    var doctor_per_pharmacy: Float,

    @field:SerializedName("convenienceperpharmacy")
    var convenience_per_pharmacy: Float,

    @field:SerializedName("viewcount")
    var viewcount: Int
): Parcelable