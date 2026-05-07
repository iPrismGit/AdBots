package com.iprism.adbots.models

import com.google.gson.annotations.SerializedName

data class ViewAdsRequest(

	@field:SerializedName("user_id")
	val userId: String,

	@field:SerializedName("auth_token")
	val authToken: String
)