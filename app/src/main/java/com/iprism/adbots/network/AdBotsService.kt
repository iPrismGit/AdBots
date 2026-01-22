package com.iprism.adbots.network

import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.models.login.LoginApiResponse
import com.iprism.adbots.models.login.LoginRequest
import com.iprism.adbots.utils.Constants
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AdBotsService {

    @GET(Constants.VIEW_ADDS_ENDPOINT)
    suspend fun fetchViewAds() : ViewAdsApiResponse

    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun login(@Body loginRequest: LoginRequest) : LoginApiResponse
}