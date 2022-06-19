package com.jms.searchpharmacy.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jms.a20220602_navermap.data.model.GeoInfo
import com.jms.searchpharmacy.data.api.RetrofitInstance.kakaoSearchApi
import com.jms.searchpharmacy.data.api.RetrofitInstance.naverMapApi

import com.jms.searchpharmacy.data.api.RetrofitInstance.serverApi

import com.jms.searchpharmacy.data.db.SearchPharDatabase
import com.jms.searchpharmacy.data.model.kakaokeyword.KakaoKeywResponse
import com.jms.searchpharmacy.data.model.reversegeo.ReverseGeoInfo
import com.jms.searchpharmacy.data.model.server.*

import retrofit2.Call
import retrofit2.Response

class MainRepository(private val db: SearchPharDatabase) {
    suspend fun searchGeoInfo(
        query: String
    ): Response<GeoInfo> {
        return naverMapApi.searchGeoInfo(query)
    }

    suspend fun convertCoordsToAddr(
        coords: String
    ): Response<ReverseGeoInfo> {
        return naverMapApi.convertCoordsToAddr(coords)
    }

    suspend fun insertPharLocation(pl: PharmacyLocation) {
        db.searchPharDao().insertPL(pl)
    }

    suspend fun deletePharLocation(pl: PharmacyLocation) {
        db.searchPharDao().deletePL(pl)
    }

    fun getDongBySearch(stationName: String): Call<List<Station>> {
        return serverApi.getDongBySearch(stationName)
    }

    fun getFavoritePharLocations(): LiveData<List<PharmacyLocation>> {
        return db.searchPharDao().getFavoritePharLocation()
    }

    fun getPharLocation(index: Int): LiveData<PharmacyLocation?> {
        return db.searchPharDao().getPharLocation(index)
    }


    fun fetchLines(): Call<List<Line>> {
        return serverApi.lines
    }

    fun fetchPLs(dongName: String): Call<List<PharmacyLocation>> {
        return serverApi.getPLList(dongName)
    }

    fun fetchPLByKey(index: Int): Call<PharmacyLocation> {
        return serverApi.getPLDetail(index)
    }


    fun fetchConvList(primaryKey: Int): Call<List<Convenience>> {
        return serverApi.getConvenienceList(primaryKey)
    }

    fun fetchHospList(primaryKey: Int): Call<List<Hospital>> {
        return serverApi.getHospitalList(primaryKey)
    }

    fun fetchPharList(primaryKey: Int): Call<List<Pharmacy>> {
        return serverApi.getPharmacyList(primaryKey)
    }

    fun fetchPLsTop5(): Call<List<PharmacyLocation>> {
        return serverApi.getTop5()
    }

    suspend fun searchByKeyword(query: String, category: String="SW8"): Response<KakaoKeywResponse> {
        return kakaoSearchApi.searchLocal(query, category)
    }
}