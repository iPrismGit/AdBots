package com.iprism.adbots.models.updatedevicestatus

import com.google.gson.annotations.SerializedName

data class UpdateDeviceStatusApiResponse(

	@field:SerializedName("response")
	val response: Response,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Boolean
)

data class Response(
	val any: Any? = null
)
