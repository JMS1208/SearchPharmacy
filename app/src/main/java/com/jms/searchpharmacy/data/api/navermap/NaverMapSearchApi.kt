package com.jms.searchpharmacy.data.api.navermap

import com.jms.a20220602_navermap.data.model.GeoInfo
import com.jms.searchpharmacy.data.model.reversegeo.ReverseGeoInfo
import com.jms.searchpharmacy.util.Constants.CLIENT_ID
import com.jms.searchpharmacy.util.Constants.CLIENT_SECRET
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverMapSearchApi {
    @Headers("X-NCP-APIGW-API-KEY-ID: $CLIENT_ID"
        ,"X-NCP-APIGW-API-KEY: $CLIENT_SECRET")
    @GET("map-geocode/v2/geocode")
    suspend fun searchGeoInfo(
        @Query("query") query: String,
    ): Response<GeoInfo>

    @Headers("X-NCP-APIGW-API-KEY-ID: $CLIENT_ID"
        ,"X-NCP-APIGW-API-KEY: $CLIENT_SECRET")
    @GET("map-reversegeocode/v2/gc?output=json")
    suspend fun convertCoordsToAddr(
        @Query("coords") coords: String,
    ): Response<ReverseGeoInfo>
}