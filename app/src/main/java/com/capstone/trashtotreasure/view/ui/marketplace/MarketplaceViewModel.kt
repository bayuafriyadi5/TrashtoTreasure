package com.capstone.trashtotreasure.view.ui.marketplace

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.capstone.trashtotreasure.model.data.remote.response.addProduct.AddProductResponse
import com.capstone.trashtotreasure.model.data.remote.response.getPenjualEmail.GetPenjualEmailResponse
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
import com.capstone.trashtotreasure.model.data.repository.MarketRepository
import com.capstone.trashtotreasure.model.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject


@HiltViewModel
@ExperimentalPagingApi
class MarketplaceViewModel @Inject constructor(
    private val marketRepository: MarketRepository,
    private val userRepository: UserRepository,
): ViewModel() {

    suspend fun getAllProducts(token: String): LiveData<ProductResponse?> =
        marketRepository.getAllProducts(token)

    suspend fun getUser(token: String): LiveData<GetUserResponse?> =
        userRepository.getUser(token)

    suspend fun getPenjualByEmail(token: String, email: String): LiveData<GetPenjualEmailResponse> =
        userRepository.getPenjualByEmail(token, email)

    suspend fun getProductByPenjual(token: String, idPenjual: String): LiveData<ProductByPenjualResponse> =
        marketRepository.getProductByPenjual(token, idPenjual)

    suspend fun getProductByName(token: String, namaProduk: String): LiveData<ProductByNameResponse?> =
        marketRepository.getProductByName(token, namaProduk)

    suspend fun getProductById(token: String, idProduk: String): LiveData<GetProductByIdResponse?> =
        marketRepository.getProductById(token, idProduk)

    suspend fun addProduct(token: String,
                       nama: String, price: String, stok: String, desc: String, photoUrl: File,
    ): LiveData<Result<AddProductResponse>> = marketRepository.addProduct(token, nama, price, stok, desc,  photoUrl)

    suspend fun updateProduct(
        token: String,
        idProduk: String,
        nama: String,
        price: String,
        stok: String,
        desc: String,
        photoUrl: File
    ): LiveData<Result<AddProductResponse>> = marketRepository.updateProduct(token, idProduk, nama, price, stok, desc, photoUrl)

    suspend fun updateProdukNoImage(
        token: String,
        idProduk: String,
        nama: String,
        price: String,
        stok: String,
        desc: String,
    ): LiveData<Result<AddProductResponse>> = marketRepository.updateProdukNoImage(token, idProduk, nama, price, stok, desc)


    suspend fun transaksi(token: String,
                              totalHarga: String, idPenjual: String, idProduk: String, qty: String, alamat: String,): LiveData<Result<TransaksiResponse>> =
        marketRepository.transaksi(token, totalHarga, idPenjual, idProduk, qty, alamat)

    suspend fun updateTransaksiInvoice(token: String, idTransaksi: String,  invoiceId: String, invoiceUrl: String, statusPesanan: String): LiveData<Result<TransaksiResponse>> =
        marketRepository.updateTransaksiInvoice(token, idTransaksi ,invoiceId, invoiceUrl, statusPesanan)


    suspend fun payment(amaount: String): LiveData<Result<PaymentResponse>> =
        marketRepository.payment(amaount)

    suspend fun getTransaksiByPembeli(token: String, idPembeli: String): LiveData<GetTransaksiByPembeliResponse> =
        marketRepository.getTransaksiByPembeli(token, idPembeli)

    suspend fun getTransaksiByPenjual(token: String, idPenjual: String): LiveData<GetTransaksiByPenjualResponse> =
        marketRepository.getTransaksiByPenjual(token, idPenjual)

    suspend fun getTransaksiById(token: String, idTransaksi: String): LiveData<GetTransaksiByIdResponse?> =
        marketRepository.getTransaksiById(token, idTransaksi)

    suspend fun transaksiSelesai(token: String, idTransaksi: String,  invoiceId: String, invoiceUrl: String, statusPesanan: String): LiveData<Result<TransaksiResponse>> =
        marketRepository.transaksiSelesai(token, idTransaksi ,invoiceId, invoiceUrl, statusPesanan)

    suspend fun pesananDikirim(token: String, idTransaksi: String,  invoiceId: String, invoiceUrl: String, statusPesanan: String): LiveData<Result<TransaksiResponse>> =
        marketRepository.pesananDikirim(token, idTransaksi ,invoiceId, invoiceUrl, statusPesanan)

    fun checkIfTokenAvailable(): LiveData<String?> = marketRepository.getToken()

}