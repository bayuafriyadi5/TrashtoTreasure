package com.capstone.trashtotreasure.model.data.remote.retrofit

import com.capstone.trashtotreasure.model.data.remote.response.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("articles")
     suspend fun getArticle(): NewsResponse
}