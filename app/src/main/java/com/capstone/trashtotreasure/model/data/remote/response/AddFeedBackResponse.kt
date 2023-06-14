package com.capstone.trashtotreasure.model.data.remote.response

import com.google.gson.annotations.SerializedName

data class AddFeedBackResponse(

	@field:SerializedName("payload")
	val payload: Payload? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class Feedback(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("isSuccess")
	val isSuccess: Boolean? = null,

	@field:SerializedName("timestamp")
	val timestamp: String? = null
)

data class Payloads(

	@field:SerializedName("feedback")
	val feedback: Feedback? = null
)
