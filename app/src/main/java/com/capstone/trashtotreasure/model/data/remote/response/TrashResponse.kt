package com.capstone.trashtotreasure.model.data.remote.response

import com.google.gson.annotations.SerializedName

data class TrashResponse(

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("file_urls")
	val fileUrls: List<String?>? = null
)
