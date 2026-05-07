package com.iprism.adbots.repository

import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.models.ViewAdsRequest
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusApiResponse
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusRequest
import com.iprism.adbots.network.AdBotsApi

class AdsRepository {

    private val apiService = AdBotsApi.adBotsService

    suspend fun fetchAds(req : ViewAdsRequest): ViewAdsApiResponse {
        return apiService.fetchViewAds(req)
    }

    suspend fun updateDevoiceStatus(request : UpdateDeviceStatusRequest): UpdateDeviceStatusApiResponse {
        return apiService.updateDeviceStatus(request)
    }
}
