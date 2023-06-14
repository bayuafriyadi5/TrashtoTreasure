package com.capstone.trashtotreasure.model.data.remote.retrofit

import com.capstone.trashtotreasure.model.data.remote.response.TrashResponse
import okhttp3.MultipartBody
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceML {

    @Multipart
    @POST("plastik")
    suspend fun uploadPlastic(
        @Part file: MultipartBody.Part
    ): TrashResponse

    @Multipart
    @POST("metal")
    suspend fun uploadMetal(
        @Part file: MultipartBody.Part
    ): TrashResponse

    @Multipart
    @POST("kaca")
    suspend fun uploadKaca(
        @Part file: MultipartBody.Part
    ): TrashResponse

    @Multipart
    @POST("kain")
    suspend fun uploadKain(
        @Part file: MultipartBody.Part
    ): TrashResponse
}