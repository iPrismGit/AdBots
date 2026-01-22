package com.iprism.adbots.models.login

import com.google.gson.annotations.SerializedName

data class LoginRequest(

	@field:SerializedName("user_name")
	val userName: String,

	@field:SerializedName("token")
	val token: String
)
