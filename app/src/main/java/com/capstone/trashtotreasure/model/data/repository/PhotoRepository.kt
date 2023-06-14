package com.capstone.trashtotreasure.model.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.trashtotreasure.model.data.remote.response.TrashResponse
import com.capstone.trashtotreasure.utils.Result
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiServiceML
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class PhotoRepository@Inject constructor(
    private val apiServiceML: ApiServiceML
) {
    fun postSnapPlastic(file: File): LiveData<Result<TrashResponse>> = liveData {
        emit(Result.Loading)

        val imageMediaType = "image".toMediaTypeOrNull()
        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody(imageMediaType)
        )

        try {
            val response = apiServiceML.uploadPlastic(imageMultiPart)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postSnapMetal(file: File): LiveData<Result<TrashResponse>> = liveData {
        emit(Result.Loading)

        val imageMediaType = "image".toMediaTypeOrNull()
        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody(imageMediaType)
        )

        try {
            val response = apiServiceML.uploadMetal(imageMultiPart)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postSnapKaca(file: File): LiveData<Result<TrashResponse>> = liveData {
        emit(Result.Loading)

        val imageMediaType = "image".toMediaTypeOrNull()
        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody(imageMediaType)
        )

        try {
            val response = apiServiceML.uploadKaca(imageMultiPart)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }

    fun postSnapKain(file: File): LiveData<Result<TrashResponse>> = liveData {
        emit(Result.Loading)

        val imageMediaType = "image".toMediaTypeOrNull()
        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "image",
            file.name,
            file.asRequestBody(imageMediaType)
        )

        try {
            val response = apiServiceML.uploadKain(imageMultiPart)
            emit(Result.Success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.Error(e.message.toString()))
        }
    }
}