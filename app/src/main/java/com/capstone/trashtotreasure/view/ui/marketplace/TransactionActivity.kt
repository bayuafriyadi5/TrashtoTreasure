package com.capstone.trashtotreasure.view.ui.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityEditProfileBinding
import com.capstone.trashtotreasure.databinding.ActivityTransactionBinding
import com.capstone.trashtotreasure.view.ui.adapter.ProductsAdapter
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.capstone.trashtotreasure.view.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
@ExperimentalPagingApi
class TransactionActivity : AppCompatActivity() {

    private val binding: ActivityTransactionBinding by lazy {
        ActivityTransactionBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()
    private var token: String = "null"
    private var idProduk: String = "null"
    private var alamat: String = "null"
    private var value: Int = 1
    private var hargaProduk: Int = 1
    private var stokProduk: Int = 1
    private var idPenjual: Int = 1
    private var idPenjualCheck: Int = 1
    private var totalPrice: Int = 1
    private var sisaStok: Int = 1
    private var  id_transaksi: String = "null"
    private var email: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        idProduk = intent.getStringExtra("id_produk").toString()

        marketplaceViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"
            if (token != "null") {
                this.lifecycleScope.launch {
                    marketplaceViewModel.getUser(token!!)
                        .observe(this@TransactionActivity) {
                            it?.payload?.let {
                                email = it.email
                                loadPenjualData(email)
                                loadData()
                            }
                        }
                }
            }
        }

        binding.btnIncrease.setOnClickListener {
            if (value < stokProduk) {
                value++
                binding.tvQty.text = value.toString()
                updateTotalPrice()
            }
        }

        binding.btnDecrease.setOnClickListener {
            if (value > 1) {
                value--
                binding.tvQty.text = value.toString()
                updateTotalPrice()
            }
        }

        binding.btnPayment.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            transaksi()
        }
    }

    private fun loadPenjualData(email: String?) {
        email?.let {
            this.lifecycleScope.launch {
                val userLiveData = withContext(Dispatchers.IO) { marketplaceViewModel.getPenjualByEmail(token, it) }
                userLiveData.observe(this@TransactionActivity) { profile ->
                    if (profile != null && this@TransactionActivity.lifecycle.currentState.isAtLeast(
                            Lifecycle.State.STARTED)) {
                        val emailPenjual =profile.payload?.email
                        if (emailPenjual != null) {
                            idPenjualCheck = profile.payload.idPenjual!!
                            binding.loadingShimmerProfile.visibility = View.INVISIBLE
                        }
                    } else {
                        Toast.makeText(
                            this@TransactionActivity,
                            "Gagal Memuat Data Penjual",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    private fun loadData() {
        this.lifecycleScope.launch {
            marketplaceViewModel.getProductById(token, idProduk).observe(this@TransactionActivity) { response ->
                response?.payload?.let {
                    binding.loadingShimmerProfile.visibility = View.INVISIBLE
                    binding.cardView.visibility = View.VISIBLE
                    binding.cardAddress.visibility = View.VISIBLE
                    binding.cardView3.visibility = View.VISIBLE
                    binding.ivProduct.visibility = View.VISIBLE
                    binding.btnPayment.visibility = View.VISIBLE

                    Glide.with(binding.root.context)
                        .load(response.payload.fotoProduk)
                        .into(binding.ivProduct)
                    binding.tvNamaProduct.text = response.payload.namaProduk
                    binding.tvDescProduct.text = response.payload.descProduk
                    binding.tvProductPriceSummary.text = "Rp "+response.payload.hargaProduk.toString()

                    stokProduk = response.payload.stokProduk ?:0
                    hargaProduk = response.payload.hargaProduk ?: 0
                    idPenjual = response.payload.idPenjual ?: 0
                    sisaStok = response.payload.stokProduk ?: 0

                    if (idPenjualCheck == idPenjual && sisaStok < 1 ){
                        Toast.makeText(
                            this@TransactionActivity,
                            "This is Your Own Product",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.btnPayment.visibility = View.INVISIBLE
                        binding.btnIncrease.visibility = View.INVISIBLE
                        binding.btnDecrease.visibility = View.INVISIBLE
                        binding.tvQty.visibility = View.INVISIBLE
                        binding.tvTitleQty.text = "Stok Habis"
                    }else if ( sisaStok < 1){
                        binding.btnPayment.visibility = View.INVISIBLE
                        binding.btnIncrease.visibility = View.INVISIBLE
                        binding.btnDecrease.visibility = View.INVISIBLE
                        binding.tvQty.visibility = View.INVISIBLE
                        binding.tvTitleQty.text = "Stok Habis"

                    } else{
                        binding.btnPayment.visibility = View.VISIBLE
                    }
                    updateTotalPrice()
                    binding.loading.visibility = View.INVISIBLE
                }
            }
        }

        this.lifecycleScope.launch {
            marketplaceViewModel.getUser(token).observe(this@TransactionActivity) { response ->
                response?.payload?.let {
                    binding.tvName.text = response.payload.nama
                    binding.tvAddressDetail.text = response.payload.alamat
                    alamat = response.payload.alamat.toString()
                    binding.loading.visibility = View.INVISIBLE
                }
            }
        }
    }
    private fun updateTotalPrice() {
        totalPrice = value * hargaProduk
        binding.tvTotalPrice.text = "Rp $totalPrice"
    }


    private fun transaksi() {
        lifecycleScope.launchWhenResumed {
            launch {
                marketplaceViewModel.transaksi(token, totalPrice.toString(), idPenjual.toString(), idProduk, value.toString(), alamat)
                    .observe(this@TransactionActivity) { result ->
                        result.onSuccess {
                            id_transaksi = it.payload?.idTransaksi.toString()
                            binding.loading.visibility = View.INVISIBLE
                        }
                    }

                marketplaceViewModel.payment(totalPrice.toString())
                    .observe(this@TransactionActivity) { result ->
                        result.onSuccess {
                            val invoice_id = it.payload?.id
                            val invoice_url =  it.payload?.invoiceUrl
                            val intent = Intent(this@TransactionActivity, PaymentActivity::class.java)
                            intent.putExtra("invoice_id", invoice_id)
                            intent.putExtra("invoice_url", invoice_url)
                            intent.putExtra("id_transaksi", id_transaksi)
                            startActivity(intent)
                            finish()
                            binding.loading.visibility = View.INVISIBLE
                        }
                    }
            }
        }
    }

}