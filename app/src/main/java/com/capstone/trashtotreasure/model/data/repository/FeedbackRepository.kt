package com.capstone.trashtotreasure.model.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.trashtotreasure.model.data.local.room.NewsDao
import com.capstone.trashtotreasure.model.data.local.room.NewsDatabase
import com.capstone.trashtotreasure.model.data.remote.response.AddFeedBackResponse
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiService
import com.capstone.trashtotreasure.utils.AppExecutors
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject

class FeedbackRepository  @Inject constructor(
    private val apiService: ApiService,
) {

    suspend fun feedback(
        email: String,
        rate: String,
        text: String
    ): LiveData<Result<AddFeedBackResponse>> = liveData {
        try {
            val requestBody = ApiService.FeedbackRequestBody(email, rate, text)
            val response = apiService.postFeedback(requestBody)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> "Email already taken"
                        else -> "Register failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Register failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }
}