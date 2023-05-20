package com.jms.searchpharmacy.data.api.kakao

import com.jms.searchpharmacy.data.model.kakaokeyword.KakaoKeywResponse
import com.jms.searchpharmacy.util.Constants.KAKAO_REST_API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoSearchApi {

    @Headers("Authorization: KakaoAK $KAKAO_REST_API_KEY")
    @GET("search/keyword.json")
    suspend fun searchLocal(
        @Query("query") query: String,
        @Query("category_group_code") category: String = "SW8",
        @Query("page") page: Int = 1,

        ): Response<KakaoKeywResponse>
}