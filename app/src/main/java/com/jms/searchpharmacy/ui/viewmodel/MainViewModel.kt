package com.jms.searchpharmacy.ui.viewmodel

import android.accounts.NetworkErrorException
import android.util.Log
import android.view.animation.Transformation
import android.widget.Toast
import androidx.lifecycle.*
import com.jms.a20220602_navermap.data.model.GeoInfo
import com.jms.searchpharmacy.data.model.SingleLiveEvent
import com.jms.searchpharmacy.data.model.kakaokeyword.KakaoKeywResponse
import com.jms.searchpharmacy.data.model.reversegeo.Coords
import com.jms.searchpharmacy.data.model.reversegeo.Region
import com.jms.searchpharmacy.data.model.server.*
import retrofit2.Callback

import com.jms.searchpharmacy.repository.MainRepository
import com.naver.maps.geometry.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

//    private val _regionLiveData: MutableLiveData<Region?> = MutableLiveData()
//    val regionLiveData: LiveData<Region?> get() = _regionLiveData
    private val _regionLiveData: SingleLiveEvent<Region?> = SingleLiveEvent()
    val regionLiveData: LiveData<Region?> get() = _regionLiveData

    fun convertCoordsToAddr(latLng: LatLng) = viewModelScope.launch(Dispatchers.IO) {
        val response = mainRepository.convertCoordsToAddr("${latLng.longitude},${latLng.latitude}")

        if (response.isSuccessful) {

            response.body()?.let { body ->
                if (body.results?.size!! > 0) {

                    val region = body.results[0].region
                    _regionLiveData.postValue(region)
                } else {
                    _regionLiveData.postValue(null)
                }

            }

        }

    }

    private val _moveThisResult: SingleLiveEvent<GeoInfo?> = SingleLiveEvent()
    val moveThisResult: LiveData<GeoInfo?> get() = _moveThisResult

    fun moveThisPlace(addr: String) = viewModelScope.launch(Dispatchers.IO) {
        val response = mainRepository.searchGeoInfo(addr)

        if (response.isSuccessful) {
            response.body()?.let { body ->
                if (body.meta?.totalCount!! > 0) {
                    _moveThisResult.postValue(body)
                } else {
                    _moveThisResult.postValue(null)
                }
            }
        }
    }
//    private val _searchResult: SingleLiveEvent<List<String>> = SingleLiveEvent()
//    val searchResult: LiveData<List<String>> get() = _searchDong
//
//    fun searchResult(query: String) = viewModelScope.launch {
//        val response = mainRepository.searchByKeyword(query)
//
//        if(response.isSuccessful) {
//            response.body()?.let {
//
//                if(it.documents != null) {
//                    for(item in it.documents) {
//
//                    }
//                }
//
//            }
//        }
//
//    }

    private val _searchDong: SingleLiveEvent<List<String>> = SingleLiveEvent()
    val searchDong: LiveData<List<String>> get() = _searchDong

    fun searchDongByQuery(query: String) = viewModelScope.launch {
        val response = mainRepository.searchByKeyword(query)

        if(response.isSuccessful) {
            response.body()?.let { body->
                body.meta?.totalCount?.let { totalCnt->
                    if(totalCnt > 0 && body.documents != null) {
                        val dongList = mutableListOf<String>()
                        for(item in body.documents){
                            val dong: List<String>? = item.addressName?.split(" ")?.filterIndexed{ index, str->
                                index == 2
                            }
                            dong?.get(0)?.let {
                                dongList += dong
                            }


                        }

                        _searchDong.postValue(dongList)

                    }
                }
            }


        }

    }

    private val _searchPhar = MutableLiveData<GeoInfo>()
    val searchPhar: LiveData<GeoInfo> get() = _searchPhar


    fun searchPharLoc(query: String) = viewModelScope.launch {
        val response = mainRepository.searchGeoInfo(query)

        if (response.isSuccessful) {
            response.body()?.let { body ->
                if (body.meta?.totalCount!! > 0) {
                    _searchPhar.postValue(body)
                }
            }
        }
    }

    private val _searchConv = MutableLiveData<GeoInfo>()
    val searchConv: LiveData<GeoInfo> get() = _searchConv


    fun searchConvLoc(query: String) = viewModelScope.launch {
        val response = mainRepository.searchGeoInfo(query)

        if (response.isSuccessful) {
            response.body()?.let { body ->
                if (body.meta?.totalCount!! > 0) {
                    _searchConv.postValue(body)
                }
            }
        }
    }

    private val _searchHosp = MutableLiveData<GeoInfo>()
    val searchHosp: LiveData<GeoInfo> get() = _searchHosp


    fun searchHospLoc(query: String) = viewModelScope.launch {
        val response = mainRepository.searchGeoInfo(query)

        if (response.isSuccessful) {
            response.body()?.let { body ->
                if (body.meta?.totalCount!! > 0) {
                    _searchHosp.postValue(body)
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
                Log.d("TAG", "List<Line> Callback.onFailure called")
            }
        })
    }

    private val _fetchedPLs = MutableLiveData<List<PharmacyLocation>>()
    val fetchedPLs: LiveData<List<PharmacyLocation>> get() = _fetchedPLs

    fun fetchPLs(dongName: String) = viewModelScope.launch {
        val call = mainRepository.fetchPLs(dongName)

        call.enqueue(object : Callback<List<PharmacyLocation>> {
            override fun onResponse(
                call: Call<List<PharmacyLocation>>,
                response: Response<List<PharmacyLocation>>
            ) {
                response.body()?.let {
                    _fetchedPLs.postValue(it)

                }
            }

            override fun onFailure(call: Call<List<PharmacyLocation>>, t: Throwable) {
                Log.d("TAG", "List<Line> Callback.onFailure called")
            }

        })

    }

    private val _fetchedPharList = MutableLiveData<List<Pharmacy>>()
    val fetchedPharList: LiveData<List<Pharmacy>> get() = _fetchedPharList

    fun fetchPharList(primaryKey: Int) = viewModelScope.launch {
        val call = mainRepository.fetchPharList(primaryKey)

        call.enqueue(object : Callback<List<Pharmacy>> {
            override fun onFailure(call: Call<List<Pharmacy>>, t: Throwable) {
                Log.d("TAG", "List<Pharmacy> Callback.onFailure called")
            }

            override fun onResponse(
                call: Call<List<Pharmacy>>,
                response: Response<List<Pharmacy>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _fetchedPharList.postValue(it)
                    }
                }
            }

        })
    }


    private val _fetchedHospList = MutableLiveData<List<Hospital>>()
    val fetchedHospList: LiveData<List<Hospital>> get() = _fetchedHospList

    fun fetchHospList(primaryKey: Int) = viewModelScope.launch {
        val call = mainRepository.fetchHospList(primaryKey)

        call.enqueue(object : Callback<List<Hospital>> {
            override fun onFailure(call: Call<List<Hospital>>, t: Throwable) {
                Log.d("TAG", "List<Hospital> Callback.onFailure called")
            }

            override fun onResponse(
                call: Call<List<Hospital>>,
                response: Response<List<Hospital>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _fetchedHospList.postValue(it)
                    }
                }
            }

        })
    }

    private val _fetchedConvList = MutableLiveData<List<Convenience>>()
    val fetchedConvList: LiveData<List<Convenience>> get() = _fetchedConvList

    fun fetchConvList(primaryKey: Int) = viewModelScope.launch {
        val call = mainRepository.fetchConvList(primaryKey)

        call.enqueue(object : Callback<List<Convenience>> {
            override fun onFailure(call: Call<List<Convenience>>, t: Throwable) {
                Log.d("TAG", "List<Convenience> Callback.onFailure called")
            }

            override fun onResponse(
                call: Call<List<Convenience>>,
                response: Response<List<Convenience>>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        _fetchedConvList.postValue(it)
                    }
                }
            }

        })
    }

    private val _fetchedPLsTop5List = MutableLiveData<List<PharmacyLocation>>()
    val fetchedPLsTop5List: LiveData<List<PharmacyLocation>> get() = _fetchedPLsTop5List

    fun fetchPLsTop5() = viewModelScope.launch {
        val call = mainRepository.fetchPLsTop5()

        call.enqueue(object : Callback<List<PharmacyLocation>> {
            override fun onResponse(
                call: Call<List<PharmacyLocation>>,
                response: Response<List<PharmacyLocation>>
            ) {
                response.body()?.let {
                    _fetchedPLsTop5List.postValue(it)
                }
            }

            override fun onFailure(call: Call<List<PharmacyLocation>>, t: Throwable) {
                Log.d("TAG", "List<PharmacyLocation> Callback.onFailure called")
            }

        })

    }


    //Room
    fun savePharLocation(pl: PharmacyLocation) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.insertPharLocation(pl)
    }


    fun deletePharLocation(pl: PharmacyLocation) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.deletePharLocation(pl)
    }

    val favoritePharLocations: LiveData<List<PharmacyLocation>> get() = mainRepository.getFavoritePharLocations()

    private val plIndexLiveData = MutableLiveData<Int>()
    var plLiveData: LiveData<PharmacyLocation?> =
        Transformations.switchMap(plIndexLiveData) {

            mainRepository.getPharLocation(it)
        }

    fun loadPL(pk: Int) {

        plIndexLiveData.value = pk
    }

}