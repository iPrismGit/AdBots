package com.iprism.adbots.models.updateadreputation

import com.google.gson.annotations.SerializedName

data class UpdateAdReputationRequest(

	@field:SerializedName("ad_id")
	val adId: String,

	@field:SerializedName("user_id")
	val userId: String
)
