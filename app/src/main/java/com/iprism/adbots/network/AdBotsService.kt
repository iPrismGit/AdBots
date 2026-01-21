package com.iprism.adbots.network

import com.iprism.adbots.models.ViewAdsApiResponse
import com.iprism.adbots.utils.Constants
import retrofit2.http.GET

interface AdBotsService {

    @GET(Constants.VIEW_ADDS_ENDPOINT)
    suspend fun fetchViewAds() : ViewAdsApiResponse
}