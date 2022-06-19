package com.jms.searchpharmacy.data.api

import com.google.gson.GsonBuilder
import com.jms.searchpharmacy.data.api.kakao.KakaoSearchApi
import com.jms.searchpharmacy.data.api.navermap.NaverMapSearchApi
import com.jms.searchpharmacy.data.api.server.ServerApi
import com.jms.searchpharmacy.util.Constants.KAKAO_LOCAL_BASE_URL
import com.jms.searchpharmacy.util.Constants.NAVERMAP_BASE_URL
import com.jms.searchpharmacy.util.Constants.SERVER_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInstance {
    private val okHttpClient: OkHttpClient by lazy {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    private fun buildRetrofitMoshi(base_url: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(base_url)
            .build()
    }

    private fun buildRetrofitGson(base_url: String): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(base_url)
            .build()
    }

    val naverMapApi: NaverMapSearchApi by lazy {
        buildRetrofitMoshi(NAVERMAP_BASE_URL).create(NaverMapSearchApi::class.java)
    }

//    val naverMapReverseApi: NaverMapSearchApi by lazy {
//        buildRetrofitGson(NAVERMAP_BASE_URL).create(NaverMapSearchApi::class.java)
//    }

    val kakaoSearchApi: KakaoSearchApi by lazy {
        buildRetrofitMoshi(KAKAO_LOCAL_BASE_URL).create(KakaoSearchApi::class.java)
    }
    val serverApi: ServerApi by lazy {
        buildRetrofitGson(SERVER_BASE_URL).create(ServerApi::class.java)
    }
}