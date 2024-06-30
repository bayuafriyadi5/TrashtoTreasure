package com.capstone.trashtotreasure.model.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.trashtotreasure.model.data.local.preferences.AuthPreferences
import com.capstone.trashtotreasure.model.data.remote.response.getPenjualEmail.GetPenjualEmailResponse
import com.capstone.trashtotreasure.model.data.remote.response.getProductByPenjual.ProductByPenjualResponse
import com.capstone.trashtotreasure.model.data.remote.response.getuser.GetUserResponse
import com.capstone.trashtotreasure.model.data.remote.response.login.LoginResponse
import com.capstone.trashtotreasure.model.data.remote.response.regisSeller.RegisSellerResponse
import com.capstone.trashtotreasure.model.data.remote.response.user.UserResponse
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiServiceMain
import com.capstone.trashtotreasure.model.data.remote.retrofit.LoginRequest
import com.capstone.trashtotreasure.model.data.remote.retrofit.PenjualRequest
import com.capstone.trashtotreasure.model.data.remote.retrofit.UpdateRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiServiceMain,
    private val authPreferences: AuthPreferences
) {

    suspend fun login(
        email: String,
        password: String
    ): LiveData<Result<LoginResponse>> = liveData {
        try {
            Log.d("UserRepository", "Attempting login with email: $email")
            val loginRequest = LoginRequest(email, password)
            val response = apiService.login(loginRequest)
            Log.d("userRepository", "Login response: $response")
            if (response.payload?.token != null) {
                emit(Result.success(response))
            } else {
                emit(Result.failure(Throwable("No token received")))
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Login failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Login failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Login failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun register(
        nama: String,
        email: String,
        password: String,
        telepon: String,
        alamat: String,
        photoUrl: File,

    ): LiveData<Result<UserResponse>> = liveData {

        val textPlainMediaType = "text/plain".toMediaType()
        val imageMediaType = "image/jpeg".toMediaTypeOrNull()

        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo_url",
            photoUrl.name,
            photoUrl.asRequestBody(imageMediaType)
        )

        val namaRequestBody = nama.toRequestBody(textPlainMediaType)
        val emailRequestBody = email.toRequestBody(textPlainMediaType)
        val passwordRequestBody = password.toRequestBody(textPlainMediaType)
        val teleponRequestBody = telepon.toRequestBody(textPlainMediaType)
        val alamatRequestBody = alamat.toRequestBody(textPlainMediaType)
        try {
            Log.d("UserRepository", "Attempting registration with email: $email")
            val response = apiService.register(namaRequestBody, emailRequestBody, passwordRequestBody, teleponRequestBody, alamatRequestBody, imageMultiPart)
            Log.d("UserRepository", "Registration response: $response")
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> "Email already taken"
                        500 -> "Registration error"
                        else -> "Register failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Register failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun getUser(token: String): LiveData<GetUserResponse?> = liveData {
        try {
            val token = generateToken(token)
            val response = apiService.getUser(token)
            emit(response) // Emit ProfileResponse directly
        } catch (e: Exception) {
            e.printStackTrace()
            emit(null) // Error occurred, emit null
        }
    }

    suspend fun update(
        token: String,
        nama: String,
        email: String,
        password: String,
        telepon: String,
        alamat: String,
        photoUrl: File,

        ): LiveData<Result<UserResponse>> = liveData {

        val textPlainMediaType = "text/plain".toMediaType()
        val imageMediaType = "image/jpeg".toMediaTypeOrNull()

        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo_url",
            photoUrl.name,
            photoUrl.asRequestBody(imageMediaType)
        )
        val token = generateToken(token)
        val namaRequestBody = nama.toRequestBody(textPlainMediaType)
        val emailRequestBody = email.toRequestBody(textPlainMediaType)
        val passwordRequestBody = password.toRequestBody(textPlainMediaType)
        val teleponRequestBody = telepon.toRequestBody(textPlainMediaType)
        val alamatRequestBody = alamat.toRequestBody(textPlainMediaType)
        try {
            Log.d("UserRepository", "Attempting update with email: $email")
            val response = apiService.update(token,namaRequestBody, emailRequestBody, passwordRequestBody, teleponRequestBody, alamatRequestBody, imageMultiPart)
            Log.d("UserRepository", "update response: $response")
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        400 -> "Email already taken"
                        500 -> "Registration error"
                        else -> "Update failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Register failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun updatenoimage(
        token: String,
        nama: String,
        email: String,
        password: String,
        telepon: String,
        alamat: String,
    ): LiveData<Result<UserResponse>> = liveData {
        try {
            val authToken = generateToken(token)
            val updateRequest = UpdateRequest(nama, email, password, telepon, alamat)
            val response = apiService.updatenoimage(authToken, updateRequest)
            Log.d("UserRepository", "update response: $response")

            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("UserRepository", "update failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Update failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Update failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun regisSeller(
        token: String,
        noRekening: String,
    ): LiveData<Result<RegisSellerResponse>> = liveData {
        try {
            val authToken = generateToken(token)
            val penjualRequest = PenjualRequest(noRekening)
            val response = apiService.regisPenjual(authToken, penjualRequest)
            Log.d("UserRepository", "update response: $response")

            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("UserRepository", "update failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Register failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Update failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun updateSeller(
        token: String,
        noRekening: String,
    ): LiveData<Result<RegisSellerResponse>> = liveData {
        try {
            val authToken = generateToken(token)
            val penjualRequest = PenjualRequest(noRekening)
            val response = apiService.updatePenjual(authToken, penjualRequest)
            Log.d("UserRepository", "update response: $response")

            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("UserRepository", "update failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Update failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Update failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }


    suspend fun getPenjualByEmail(token: String, email: String): LiveData<GetPenjualEmailResponse> = liveData {
        try {
            val authToken = generateToken(token)
            val response = apiService.getPenjualEmail(authToken, email)
            emit(response)
        } catch (e: Exception) {
            Log.e("UserRepository", "Failed to get Penjual by email: ${e.message}")
            e.printStackTrace()
        }
    }


    fun getToken(): LiveData<String?> = authPreferences.getToken()

    private fun generateToken(token: String): String = "Bearer $token"
}
