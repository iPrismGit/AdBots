package com.iprism.adbots.models

import com.google.gson.annotations.SerializedName

data class ViewAdsApiResponse(

	@field:SerializedName("response")
	val response: List<ResponseItem>,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)