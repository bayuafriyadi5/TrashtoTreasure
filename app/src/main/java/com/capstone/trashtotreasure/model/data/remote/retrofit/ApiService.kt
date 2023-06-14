package com.capstone.trashtotreasure.model.data.remote.retrofit

import com.capstone.trashtotreasure.model.data.remote.response.AddFeedBackResponse
import com.capstone.trashtotreasure.model.data.remote.response.NewsResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("articles")
     suspend fun getArticle(): NewsResponse

    @POST("feedback")
    suspend fun postFeedback(@Body requestBody: FeedbackRequestBody): AddFeedBackResponse

    data class FeedbackRequestBody(
        val email: String,
        val rate: String,
        val text: String
    )
}