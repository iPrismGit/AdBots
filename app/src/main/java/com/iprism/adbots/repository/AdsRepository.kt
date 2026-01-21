package com.iprism.adbots.repository

import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.network.AdBotsApi

class AdsRepository {

    private val apiService = AdBotsApi.adBotsService

    suspend fun fetchAds(): ViewAdsApiResponse {
        return apiService.fetchViewAds()
    }
}
