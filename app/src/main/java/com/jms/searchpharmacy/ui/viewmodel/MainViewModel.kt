package com.jms.searchpharmacy.ui.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jms.a20220602_navermap.data.model.GeoInfo
import com.jms.searchpharmacy.data.model.server.*
import retrofit2.Callback

import com.jms.searchpharmacy.repository.MainRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainViewModel(
    private val mainRepository: MainRepository
): ViewModel() {



    private val _searchResult = MutableLiveData<GeoInfo>()
    val searchResult: LiveData<GeoInfo> get() = _searchResult


    fun searchGeoInfo(query: String) = viewModelScope.launch {
        val response = mainRepository.searchGeoInfo(query)

        if(response.isSuccessful) {
            response.body()?.let { body ->
                if(body.meta?.totalCount!! > 0) {
                    _searchResult.postValue(body)
                }
            }
        }
    }

    private val _fetchedLines = MutableLiveData<List<Line>>()
    val fetchedLines: LiveData<List<Line>> get() = _fetchedLines

    fun fetchLines() = viewModelScope.launch {
        val call = mainRepository.fetchLines()

        call.enqueue(object : Callback<List<Line>> {
            override fun onResponse(
                call: Call<List<Line>>,
                response: Response<List<Line>>
            ) {
                response.body()?.let {
                    _fetchedLines.postValue(it)
                }
            }

            override fun onFailure(call: Call<List<Line>>, t: Throwable) {
                Log.d("TAG","List<Line> Callback.onFailure called")
            }
        })
    }

    private val _fetchedPLs = MutableLiveData<List<PharmacyLocation>>()
    val fetchedPLs: LiveData<List<PharmacyLocation>> get() = _fetchedPLs

    fun fetchPLs(dongName: String) = viewModelScope.launch {
        val call = mainRepository.fetchPLs(dongName)

        call.enqueue(object: Callback<List<PharmacyLocation>>{
            override fun onResponse(
                call: Call<List<PharmacyLocation>>,
                response: Response<List<PharmacyLocation>>
            ) {
                response.body()?.let{
                    _fetchedPLs.postValue(it)

                }
            }

            override fun onFailure(call: Call<List<PharmacyLocation>>, t: Throwable) {
                Log.d("TAG","List<Line> Callback.onFailure called")
            }

        })

    }

    private val _fetchedPharList = MutableLiveData<List<Pharmacy>> ()
    val fetchedPharList: LiveData<List<Pharmacy>> get() = _fetchedPharList

    fun fetchPharList(primaryKey: Int) = viewModelScope.launch {
        val call = mainRepository.fetchPharList(primaryKey)

        call.enqueue(object: Callback<List<Pharmacy>>{
            override fun onFailure(call: Call<List<Pharmacy>>, t: Throwable) {
                Log.d("TAG","List<Pharmacy> Callback.onFailure called")
            }

            override fun onResponse(
                call: Call<List<Pharmacy>>,
                response: Response<List<Pharmacy>>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        _fetchedPharList.postValue(it)
                    }
                }
            }

        })
    }

    private val _fetchedHospList = MutableLiveData<List<Hospital>> ()
    val fetchedHospList: LiveData<List<Hospital>> get() = _fetchedHospList

    fun fetchHospList(primaryKey: Int) = viewModelScope.launch {
        val call = mainRepository.fetchHospList(primaryKey)

        call.enqueue(object: Callback<List<Hospital>>{
            override fun onFailure(call: Call<List<Hospital>>, t: Throwable) {
                Log.d("TAG","List<Hospital> Callback.onFailure called")
            }

            override fun onResponse(
                call: Call<List<Hospital>>,
                response: Response<List<Hospital>>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        _fetchedHospList.postValue(it)
                    }
                }
            }

        })
    }

    private val _fetchedConvList = MutableLiveData<List<Convenience>> ()
    val fetchedConvList: LiveData<List<Convenience>> get() = _fetchedConvList

    fun fetchConvList(primaryKey: Int) = viewModelScope.launch {
        val call = mainRepository.fetchConvList(primaryKey)

        call.enqueue(object: Callback<List<Convenience>>{
            override fun onFailure(call: Call<List<Convenience>>, t: Throwable) {
                Log.d("TAG","List<Convenience> Callback.onFailure called")
            }

            override fun onResponse(
                call: Call<List<Convenience>>,
                response: Response<List<Convenience>>
            ) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        _fetchedConvList.postValue(it)
                    }
                }
            }

        })
    }

}