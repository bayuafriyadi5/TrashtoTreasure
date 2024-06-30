package com.capstone.trashtotreasure.model.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.capstone.trashtotreasure.model.data.local.preferences.AuthPreferences
import com.capstone.trashtotreasure.model.data.remote.response.addProduct.AddProductResponse
import com.capstone.trashtotreasure.model.data.remote.response.getProductById.GetProductByIdResponse
import com.capstone.trashtotreasure.model.data.remote.response.getProductByName.ProductByNameResponse
import com.capstone.trashtotreasure.model.data.remote.response.getProductByPenjual.ProductByPenjualResponse
import com.capstone.trashtotreasure.model.data.remote.response.getTransaksiById.GetTransaksiByIdResponse
import com.capstone.trashtotreasure.model.data.remote.response.getTransaksiByPembeli.GetTransaksiByPembeliResponse
import com.capstone.trashtotreasure.model.data.remote.response.getTransaksiByPenjual.GetTransaksiByPenjualResponse
import com.capstone.trashtotreasure.model.data.remote.response.getuser.GetUserResponse
import com.capstone.trashtotreasure.model.data.remote.response.payment.PaymentResponse
import com.capstone.trashtotreasure.model.data.remote.response.product.ProductResponse
import com.capstone.trashtotreasure.model.data.remote.response.transaksi.TransaksiResponse
import com.capstone.trashtotreasure.model.data.remote.response.user.UserResponse
import com.capstone.trashtotreasure.model.data.remote.retrofit.ApiServiceMain
import com.capstone.trashtotreasure.model.data.remote.retrofit.PaymentRequest
import com.capstone.trashtotreasure.model.data.remote.retrofit.TransaksiRequest
import com.capstone.trashtotreasure.model.data.remote.retrofit.UpdateProdukRequest
import com.capstone.trashtotreasure.model.data.remote.retrofit.UpdateRequest
import com.capstone.trashtotreasure.model.data.remote.retrofit.UpdateTransaksiInvoiceRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File
import java.net.SocketTimeoutException
import javax.inject.Inject

class MarketRepository@Inject constructor(
    private val apiService: ApiServiceMain,
    private val authPreferences: AuthPreferences
) {

    suspend fun getAllProducts(token: String): LiveData<ProductResponse?> = liveData {
        try {
            val token = generateToken(token)
            val response = apiService.getAllProducts(token)

            response.payload?.forEach { product ->
                Log.d("MarketRepository", "Product: ${product?.idProduk}, ${product?.hargaProduk}")
            }
            emit(response)
        } catch (e: Exception) {
            e.printStackTrace()
            emit(null)
        }
    }


    suspend fun addProduct(
        token: String,
        namaProduk: String,
        hargaProduk: String,
        stokProduk: String,
        descProduk: String,
        fotoProduk: File
    ): LiveData<Result<AddProductResponse>> = liveData {

        val textPlainMediaType = "text/plain".toMediaType()
        val imageMediaType = "image/jpeg".toMediaTypeOrNull()

        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "foto_produk",
            fotoProduk.name,
            fotoProduk.asRequestBody(imageMediaType)
        )
        val token = generateToken(token)
        val namaRequestBody = namaProduk.toRequestBody(textPlainMediaType)
        val priceRequestBody = hargaProduk.toRequestBody(textPlainMediaType)
        val stokRequestBody = stokProduk.toRequestBody(textPlainMediaType)
        val descRequestBody = descProduk.toRequestBody(textPlainMediaType)
        try {
            Log.d("MarketRepository", "Attempting addProduct with token: $token")
            val response = apiService.addProduct(token, namaRequestBody, priceRequestBody, stokRequestBody, descRequestBody, imageMultiPart)
            Log.d("MarketRepository", "addProduct response: $response")
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    Log.e("MarketRepository", "HttpException: ${e.response()?.errorBody()?.string()}")
                    when (e.code()) {
                        400 -> "Id already taken"
                        500 -> "Add Product error"
                        else -> "Add failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> {
                    Log.e("MarketRepository", "Exception: ${e.localizedMessage}")
                    "Register failed, please try again later."
                }
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun updateProduct(
        token: String,
        idProduk: String,
        nama: String,
        price: String,
        stok: String,
        desc: String,
        photoUrl: File
    ): LiveData<Result<AddProductResponse>> = liveData {
        val textPlainMediaType = "text/plain".toMediaType()
        val imageMediaType = "image/jpeg".toMediaTypeOrNull()

        val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "foto_produk",
            photoUrl.name,
            photoUrl.asRequestBody(imageMediaType)
        )
        val authToken = generateToken(token)
        val namaRequestBody = nama.toRequestBody(textPlainMediaType)
        val idProdukRequestBody = idProduk.toRequestBody(textPlainMediaType)
        val hargaRequestBody = price.toRequestBody(textPlainMediaType)
        val stokRequestBody = stok.toRequestBody(textPlainMediaType)
        val descRequestBody = desc.toRequestBody(textPlainMediaType)

        try {
            Log.d("MarketRepository", "Updating product with ID: $idProduk")
            val response = apiService.updateProduct(
                authToken, idProduk, namaRequestBody, hargaRequestBody, stokRequestBody,
                descRequestBody, imageMultiPart
            )
            Log.d("MarketRepository", "Update response: $response")
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> when (e.code()) {
                    400 -> "Invalid request"
                    500 -> "Server error"
                    else -> "Update failed, please try again later."
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Update failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun updateProdukNoImage(
        token: String,
        idProduk: String,
        nama: String,
        price: String,
        stok: String,
        desc: String,
    ): LiveData<Result<AddProductResponse>> = liveData {
        try {
            val authToken = generateToken(token)
            val updateRequest = UpdateProdukRequest(nama, price, stok, desc)
            val response = apiService.updateproduknoimage(authToken, idProduk ,updateRequest)
            Log.d("UserRepository", "update response: $response")

            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("UserRepository", "update failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "Produk not found"
                        else -> "Update failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Update failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }


    suspend fun getProductByPenjual(token: String, idPenjual: String): LiveData<ProductByPenjualResponse> = liveData {
        try {
            val authToken = generateToken(token)
            val response = apiService.getProductByPenjual(authToken, idPenjual)
            emit(response)
        } catch (e: Exception) {
            Log.e("marketRepository", "Failed to get Produk by id_penjual: ${e.message}")
            e.printStackTrace()
            emit(ProductByPenjualResponse(
                statusCode = 500,  // Assuming 500 is the status code for errors
                message = "Failed to load data: ${e.message}",
                payload = emptyList()  // Assuming empty list for payload in case of error
            ))
        }
    }


    suspend fun getProductByName(token: String, namaProduk: String): LiveData<ProductByNameResponse?> = liveData {
        try {
            val authToken = generateToken(token)
            val response = apiService.getProductByName(authToken, namaProduk)
            emit(response)
        } catch (e: Exception) {
            Log.e("marketRepository", "Failed to get Produk by name: ${e.message}")
            e.printStackTrace()
            emit(null)
        }
    }

    suspend fun getProductById(token: String, idProduk: String): LiveData<GetProductByIdResponse?> = liveData {
        try {
            val authToken = generateToken(token)
            val response = apiService.getProductById(authToken, idProduk)
            emit(response)
        } catch (e: Exception) {
            Log.e("marketRepository", "Failed to get Produk by id: ${e.message}")
            e.printStackTrace()
            emit(null)
        }
    }

    suspend fun transaksi(
        token: String,
        totalHarga: String,
        idPenjual: String,
        idProduk: String,
        qty: String,
        alamat: String,
    ): LiveData<Result<TransaksiResponse>> = liveData {
        try {
            val authToken = generateToken(token)
            val transaksieRequest = TransaksiRequest(totalHarga, idPenjual, idProduk, qty, alamat)
            val response = apiService.transaksi(authToken, transaksieRequest)
            Log.d("MarketRepository", "transaksi response: $response")

            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("MarketRepository", "transaksi failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Transaksi failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Transaksi failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun updateTransaksiInvoice(
        token: String,
        idTransaksi: String,
        invoiceId: String,
        invoiceUrl: String,
        statusPesanan: String
    ): LiveData<Result<TransaksiResponse>> = liveData {
        try {
            val authToken = generateToken(token)
            val statusPesanan = "Disiapkan"
            val updateTransaksiInvoiceeRequest = UpdateTransaksiInvoiceRequest(idTransaksi, invoiceId, invoiceUrl,statusPesanan)
            val response = apiService.updateTransaksiInvoice(authToken, updateTransaksiInvoiceeRequest)
            Log.d("MarketRepository", "transaksi response: $response")

            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("MarketRepository", "transaksi failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Transaksi failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Transaksi failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun transaksiSelesai(
        token: String,
        idTransaksi: String,
        invoiceId: String,
        invoiceUrl: String,
        statusPesanan: String
    ): LiveData<Result<TransaksiResponse>> = liveData {
        try {
            val authToken = generateToken(token)
            val statusPesanan = "Sampai"
            val updateTransaksiInvoiceeRequest = UpdateTransaksiInvoiceRequest(idTransaksi, invoiceId, invoiceUrl,statusPesanan)
            val response = apiService.updateTransaksiInvoice(authToken, updateTransaksiInvoiceeRequest)
            Log.d("MarketRepository", "transaksi response: $response")

            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("MarketRepository", "transaksi failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Transaksi failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Transaksi failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun pesananDikirim(
        token: String,
        idTransaksi: String,
        invoiceId: String,
        invoiceUrl: String,
        statusPesanan: String
    ): LiveData<Result<TransaksiResponse>> = liveData {
        try {
            val authToken = generateToken(token)
            val statusPesanan = "Dikirim"
            val updateTransaksiInvoiceeRequest = UpdateTransaksiInvoiceRequest(idTransaksi, invoiceId, invoiceUrl,statusPesanan)
            val response = apiService.updateTransaksiInvoice(authToken, updateTransaksiInvoiceeRequest)
            Log.d("MarketRepository", "transaksi response: $response")

            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("MarketRepository", "transaksi failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Transaksi failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Transaksi failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun payment(
        amount: String,
    ): LiveData<Result<PaymentResponse>> = liveData {
        try {
            val paymentRequest = PaymentRequest(amount)
            val response = apiService.payment(paymentRequest)
            Log.d("MarketRepository", "payment response: $response")
            emit(Result.success(response))
        } catch (e: Exception) {
            Log.e("MarketRepository", "payment failed: ${e.message}")
            e.printStackTrace()
            val errorMessage = when (e) {
                is HttpException -> {
                    when (e.code()) {
                        401 -> "Invalid email or password"
                        404 -> "User not found"
                        else -> "Payment failed, please try again later."
                    }
                }
                is SocketTimeoutException -> "Connection timed out, please try again later."
                else -> "Payment failed, please try again later."
            }
            emit(Result.failure(Throwable(errorMessage, e)))
        }
    }

    suspend fun getTransaksiByPembeli(token: String, idPembeli: String): LiveData<GetTransaksiByPembeliResponse> = liveData {
        try {
            val authToken = generateToken(token)
            val response = apiService.getTransaksiByPembeli(authToken, idPembeli)
            emit(response)
        } catch (e: Exception) {
            Log.e("marketRepository", "Failed to get Transaksi by id_pembeli: ${e.message}")
            e.printStackTrace()
            emit(GetTransaksiByPembeliResponse(
                statusCode = 500,  // Assuming 500 is the status code for errors
                message = "Failed to load data: ${e.message}",
                payload = emptyList()  // Assuming empty list for payload in case of error
            ))
        }
    }

    suspend fun getTransaksiByPenjual(token: String, idPenjual: String): LiveData<GetTransaksiByPenjualResponse> = liveData {
        try {
            val authToken = generateToken(token)
            val response = apiService.getTransaksiByPenjual(authToken, idPenjual)
            emit(response)
        } catch (e: Exception) {
            Log.e("marketRepository", "Failed to get Transaksi by id_pembeli: ${e.message}")
            e.printStackTrace()
            emit(GetTransaksiByPenjualResponse(
                statusCode = 500,  // Assuming 500 is the status code for errors
                message = "Failed to load data: ${e.message}",
                payload = emptyList()  // Assuming empty list for payload in case of error
            ))

        }
    }


    suspend fun getTransaksiById(token: String, idTransaksi: String): LiveData<GetTransaksiByIdResponse?> = liveData {
        try {
            val authToken = generateToken(token)
            val response = apiService.getTransaksiById(authToken, idTransaksi)
            emit(response)
        } catch (e: Exception) {
            Log.e("marketRepository", "Failed to get Transaksi by id: ${e.message}")
            e.printStackTrace()

        }
    }

    fun getToken(): LiveData<String?> = authPreferences.getToken()

    private fun generateToken(token: String): String = "Bearer $token"

}