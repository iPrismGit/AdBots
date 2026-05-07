package com.iprism.adbots.models

import com.google.gson.annotations.SerializedName

data class ResponseItem(

	@field:SerializedName("ads")
	val ads: List<Ads>,
)

data class Ads(
	@field:SerializedName("created_on")
	val createdOn: String,

	@field:SerializedName("ad_link")
	val adLink: String,

	@field:SerializedName("id")
	val id: String
)