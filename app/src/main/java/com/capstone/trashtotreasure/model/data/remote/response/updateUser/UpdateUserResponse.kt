package com.capstone.trashtotreasure.model.data.remote.response.updateUser

import com.google.gson.annotations.SerializedName

data class UpdateUserResponse(

	@field:SerializedName("status_code")
	val statusCode: Int? = null,

	@field:SerializedName("pagination")
	val pagination: Pagination? = null,

	@field:SerializedName("payload")
	val payload: Payload? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Pagination(

	@field:SerializedName("next")
	val next: String? = null,

	@field:SerializedName("max")
	val max: String? = null,

	@field:SerializedName("prev")
	val prev: String? = null
)

data class Payload(

	@field:SerializedName("isSuccess")
	val isSuccess: Int? = null
)
