package com.iprism.adbots.models.updatedevicestatus

import com.google.gson.annotations.SerializedName

data class UpdateDeviceStatusRequest(

	@field:SerializedName("device_status")
	val deviceStatus: String,

	@field:SerializedName("user_id")
	val userId: String
)
