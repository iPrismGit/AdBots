package com.iprism.adbots.repository

import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusApiResponse
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusRequest
import com.iprism.adbots.network.AdBotsApi

class AdsRepository {

    private val apiService = AdBotsApi.adBotsService

    suspend fun fetchAds(): ViewAdsApiResponse {
        return apiService.fetchViewAds()
    }

    suspend fun updateDevoiceStatus(request : UpdateDeviceStatusRequest): UpdateDeviceStatusApiResponse {
        return apiService.updateDeviceStatus(request)
    }
}
