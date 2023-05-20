package com.jms.searchpharmacy.util

object Constants {
    const val CLIENT_ID = "yo55hrtdpw"
    const val CLIENT_SECRET = "chzlZ6A18o7c3XcHBCQ0JS69FZMTr8uQhUYpcB1S"
    const val NAVERMAP_BASE_URL="https://naveropenapi.apigw.ntruss.com/"
    const val IP = "192.168.0.12:8080"
    const val SERVER_BASE_URL = "http://$IP/api/pharmacy/"
    const val KAKAO_LOCAL_BASE_URL = "https://dapi.kakao.com/v2/local/"
    const val KAKAO_REST_API_KEY = "2f6b526cd7ec10faa4606d0b4dd60daa"

    const val PERMISSION_REQUEST_CODE = 10

    val stationNamePattern = "^[가-힇|0-9]*역".toRegex()

    val dongNamePattern = "^[가-힇|0-9]*[동|가]".toRegex()

    const val LAST_MY_PLACE = "last_my_place_info"
    const val DONG_NAME_NAV_ARGS = "dongName"
}