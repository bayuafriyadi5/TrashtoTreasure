package com.capstone.trashtotreasure.model.data.remote.retrofit

import com.capstone.trashtotreasure.model.data.remote.response.addProduct.AddProductResponse
import com.capstone.trashtotreasure.model.data.remote.response.getPenjualEmail.GetPenjualEmailResponse
import com.capstone.trashtotreasure.model.data.remote.response.getProductById.GetProductByIdResponse
import com.capstone.trashtotreasure.model.data.remote.response.getProductByName.ProductByNameResponse
import com.capstone.trashtotreasure.model.data.remote.response.getProductByPenjual.ProductByPenjualResponse
import com.capstone.trashtotreasure.model.data.remote.response.getTransaksiById.GetTransaksiByIdResponse
import com.capstone.trashtotreasure.model.data.remote.response.getTransaksiByPembeli.GetTransaksiByPembeliResponse
import com.capstone.trashtotreasure.model.data.remote.response.getTransaksiByPenjual.GetTransaksiByPenjualResponse
import com.capstone.trashtotreasure.model.data.remote.response.getuser.GetUserResponse
import com.capstone.trashtotreasure.model.data.remote.response.login.LoginResponse
import com.capstone.trashtotreasure.model.data.remote.response.payment.PaymentResponse
import com.capstone.trashtotreasure.model.data.remote.response.product.ProductResponse
import com.capstone.trashtotreasure.model.data.remote.response.regisSeller.RegisSellerResponse
import com.capstone.trashtotreasure.model.data.remote.response.transaksi.TransaksiResponse
import com.capstone.trashtotreasure.model.data.remote.response.user.UserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServiceMain {

    @POST("pembeli/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

    @POST("pembeli/register")
    @Multipart
    suspend fun register(
        @Part("nama") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("telepon") telepon: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part photoUrl: MultipartBody.Part,
    ): UserResponse

    @PUT("pembeli")
    @Multipart
    suspend fun update(
        @Header("Authorization") token: String,
        @Part("nama") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("telepon") telepon: RequestBody,
        @Part("alamat") alamat: RequestBody,
        @Part photoUrl: MultipartBody.Part,
    ): UserResponse

    @POST("produk")
    @Multipart
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Part("nama_produk") namaProduk: RequestBody,
        @Part("harga_produk") hargaProduk: RequestBody,
        @Part("stok_produk") stokProduk: RequestBody,
        @Part("desc_produk") descProduk: RequestBody,
        @Part fotoProduk: MultipartBody.Part,
    ): AddProductResponse

    @PUT("produk/{id_produk}")
    @Multipart
    suspend fun updateProduct(
        @Header("Authorization") token: String,
        @Path("id_produk") idProduk: String,
        @Part("nama_produk") namaProduk: RequestBody,
        @Part("harga_produk") hargaProduk: RequestBody,
        @Part("stok_produk") stokProduk: RequestBody,
        @Part("desc_produk") descProduk: RequestBody,
        @Part fotoProduk: MultipartBody.Part
    ): AddProductResponse

    @PUT("produk/noimage/{id_produk}")
    suspend fun updateproduknoimage(
        @Header("Authorization") token: String,
        @Path("id_produk") idProduk: String,
        @Body updateProdukRequest: UpdateProdukRequest
    ): AddProductResponse


    @PUT("pembeli/noimage")
    suspend fun updatenoimage(
        @Header("Authorization") token: String,
        @Body updateRequest: UpdateRequest
    ): UserResponse

    @GET("pembeli/getuser")
    suspend fun getUser(
        @Header("Authorization") token: String
    ): GetUserResponse

    @GET("penjual/find?email=")
    suspend fun getPenjualEmail(
        @Header("Authorization") token: String,
        @Query("email") email: String
    ): GetPenjualEmailResponse

    @GET("produk/findpenjual?id_penjual=")
    suspend fun getProductByPenjual(
        @Header("Authorization") token: String,
        @Query("id_penjual") idPenjual: String
    ): ProductByPenjualResponse

    @GET("produk/find?nama_produk=")
    suspend fun getProductByName(
        @Header("Authorization") token: String,
        @Query("nama_produk") namaProduk: String
    ): ProductByNameResponse

    @GET("produk/{id_produk}")
    suspend fun getProductById(
        @Header("Authorization") token: String,
        @Path("id_produk") idProduk: String
    ): GetProductByIdResponse

    @GET("produk")
    suspend fun getAllProducts(
        @Header("Authorization") token: String
    ): ProductResponse

    @POST("penjual/register")
    suspend fun regisPenjual(
        @Header("Authorization") token: String,
        @Body penjualRequest: PenjualRequest
    ): RegisSellerResponse

    @PUT("penjual")
    suspend fun updatePenjual(
        @Header("Authorization") token: String,
        @Body penjualRequest: PenjualRequest
    ): RegisSellerResponse

    @POST("transaksi")
    suspend fun transaksi(
        @Header("Authorization") token: String,
        @Body transaksiRequest: TransaksiRequest
    ): TransaksiResponse

    @PUT("transaksi/updateinvoice")
    suspend fun updateTransaksiInvoice(
        @Header("Authorization") token: String,
        @Body updateTransaksiInvoiceRequest: UpdateTransaksiInvoiceRequest
    ): TransaksiResponse

    @POST("payment/v2/invoices")
    suspend fun payment(
        @Body paymentRequest: PaymentRequest
    ): PaymentResponse


    @GET("transaksi/findpembeli?id_pembeli=")
    suspend fun getTransaksiByPembeli(
        @Header("Authorization") token: String,
        @Query("id_pembeli") idPembeli: String
    ): GetTransaksiByPembeliResponse


    @GET("transaksi/findpenjual?id_penjual=")
    suspend fun getTransaksiByPenjual(
        @Header("Authorization") token: String,
        @Query("id_penjual") idPenjual: String
    ): GetTransaksiByPenjualResponse

    @GET("transaksi/{id_transaksi}")
    suspend fun getTransaksiById(
        @Header("Authorization") token: String,
        @Path("id_transaksi") idTransaksi: String
    ): GetTransaksiByIdResponse
}

data class UpdateRequest(
    val nama: String,
    val email: String,
    val password: String,
    val telepon: String,
    val alamat: String,
)

data class UpdateProdukRequest(
    val nama_produk: String,
    val desc_produk: String,
    val harga_produk: String,
    val stok_produk: String,
)

data class TransaksiRequest(
    val total_harga: String,
    val id_penjual: String,
    val id_produk: String,
    val qty: String,
    val alamat: String,
)

data class UpdateTransaksiInvoiceRequest(
    val id_transaksi: String,
    val invoice_id: String,
    val invoice_url: String,
    val status_pesanan: String
)


data class PenjualRequest(
    val no_rekening: String,
)


data class PaymentRequest(
    val amount: String,
)

data class LoginRequest(
    val email: String,
    val password: String
)
