package com.iprism.adbots.network

import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.models.ViewAdsRequest
import com.iprism.adbots.models.login.LoginApiResponse
import com.iprism.adbots.models.login.LoginRequest
import com.iprism.adbots.models.updateadreputation.UpdateAdReputationApiResponse
import com.iprism.adbots.models.updateadreputation.UpdateAdReputationRequest
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusApiResponse
import com.iprism.adbots.models.updatedevicestatus.UpdateDeviceStatusRequest
import com.iprism.adbots.utils.Constants
import retrofit2.http.Body
import retrofit2.http.POST

interface AdBotsService {

    @POST(Constants.VIEW_ADDS_ENDPOINT)
    suspend fun fetchViewAds(@Body req : ViewAdsRequest) : ViewAdsApiResponse

    @POST(Constants.LOGIN_ENDPOINT)
    suspend fun login(@Body loginRequest: LoginRequest) : LoginApiResponse

    @POST(Constants.UPDATE_DEVICE_STATUS_ENDPOINT)
    suspend fun updateDeviceStatus(@Body updateDeviceStatusRequest: UpdateDeviceStatusRequest) : UpdateDeviceStatusApiResponse

    @POST(Constants.UPDATE_AD_REPUTATION_ENDPOINT)
    suspend fun updateDeviceStatus(@Body req: UpdateAdReputationRequest) : UpdateAdReputationApiResponse
}