package com.iprism.adbots.repository

import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.models.login.LoginApiResponse
import com.iprism.adbots.models.login.LoginRequest
import com.iprism.adbots.network.AdBotsApi

class AuthRepository {

    private val apiService = AdBotsApi.adBotsService

    suspend fun login(request: LoginRequest): LoginApiResponse {
        return apiService.login(request)
    }
}
