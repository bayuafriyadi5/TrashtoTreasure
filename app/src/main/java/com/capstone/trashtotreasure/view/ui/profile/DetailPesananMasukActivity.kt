package com.capstone.trashtotreasure.view.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityDetailPesananMasukBinding
import com.capstone.trashtotreasure.databinding.ActivityDetailTransactionPembeliBinding
import com.capstone.trashtotreasure.view.ui.marketplace.MarketplaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
@ExperimentalPagingApi
class DetailPesananMasukActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailPesananMasukBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()
    private var token: String = "null"
    private var idPenjual: String = "null"
    private var nama: String = "null"
    private var invoice_url: String = "null"
    private var invoice_id: String = "null"
    private var id_transaksi: String = "null"
    private var statusPesanan: String = "Dikirim"
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        invoice_id = intent.getStringExtra("invoice_id") ?: "null"
        invoice_url = intent.getStringExtra("invoice_url") ?: "null"
        id_transaksi = intent.getStringExtra("id_transaksi") ?: "no_id"

        marketplaceViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"
            if (token != "null") {
                this.lifecycleScope.launch {
                    marketplaceViewModel.getUser(token!!)
                        .observe(this@DetailPesananMasukActivity) {
                            it?.payload?.let {
                                email = it.email
                                binding.loading.visibility = View.GONE
                                loadPenjualData(email)
                                binding.loadingShimmerProfile.visibility = View.INVISIBLE
                            }
                        }
                }

            }
        }

        binding.btnDeliver.setOnClickListener {
            updateTransaksiInvoice()
        }
    }
    private fun loadPenjualData(email: String?) {
        email?.let {
            this.lifecycleScope.launch {
                val userLiveData = withContext(Dispatchers.IO) { marketplaceViewModel.getPenjualByEmail(token, it) }
                userLiveData.observe(this@DetailPesananMasukActivity) { profile ->
                    if (profile != null && this@DetailPesananMasukActivity.lifecycle.currentState.isAtLeast(
                            Lifecycle.State.STARTED)) {
                        val emailPenjual =profile.payload?.email
                        if (emailPenjual != null) {
                            idPenjual = profile.payload.idPenjual.toString()
                            loadHomeData()
                            binding.loadingShimmerProfile.visibility = View.INVISIBLE
                        }
                    } else {
                        Toast.makeText(
                            this@DetailPesananMasukActivity,
                            "Gagal Memuat Data Penjual",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun loadHomeData() {
        this.lifecycleScope.launch {
            marketplaceViewModel.getTransaksiById(token!!, id_transaksi)
                .observe(this@DetailPesananMasukActivity) {
                    // Log the entire response to inspect the structure
                    Log.d("TransactionResponse", it.toString())

                    binding.tvName.visibility = View.VISIBLE
                    binding.ivProduct.visibility = View.VISIBLE
                    binding.tvNamaProduct.visibility = View.VISIBLE
                    binding.tvDate.visibility = View.VISIBLE
                    binding.tvQty.visibility = View.VISIBLE
                    binding.tvAddressDetail.visibility = View.VISIBLE
                    binding.tvStatusPesanan.visibility = View.VISIBLE
                    binding.tvTotalPrice.visibility = View.VISIBLE
                    binding.tvProductPriceSummary.visibility = View.VISIBLE
                    binding.tvDescProduct.visibility = View.VISIBLE
                    binding.tvDescTitle.visibility = View.VISIBLE
                    binding.tvTitleAddress.visibility = View.VISIBLE
                    binding.tvSummaryPrice.visibility = View.VISIBLE
                    binding.tvTitleQty.visibility = View.VISIBLE
                    binding.tvTitleTotalPrice.visibility = View.VISIBLE
                    binding.tvTitleHarga.visibility = View.VISIBLE
                    binding.btnDeliver.visibility = View.VISIBLE

                    Glide.with(this@DetailPesananMasukActivity)
                        .load(it?.payload?.produk?.fotoProduk)
                        .into(binding.ivProduct)

                    binding.tvNamaProduct.text = it?.payload?.produk?.namaProduk
                    binding.tvDescProduct.text = it?.payload?.produk?.descProduk
                    binding.tvAddressDetail.text = it?.payload?.alamat
                    binding.tvTitleAddress.text = "Address"
                    binding.tvProductPriceSummary.text = "Rp. " + it?.payload?.produk?.hargaProduk?.toString()
                    binding.tvQty.text = it?.payload?.qty?.toString()
                    binding.tvTotalPrice.text = "Rp. " +  it?.payload?.totalHarga
                    binding.tvStatusPesanan.text = it?.payload?.statusPesanan
                    binding.tvDate.text = it?.payload?.updatedAt
                    binding.tvName.text = it?.payload?.pembeli?.nama
                    invoice_url = it?.payload?.invoiceUrl.toString()
                    invoice_id = it?.payload?.invoiceId.toString()

                    it?.payload?.updatedAt?.let {
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID"))
                        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
                        outputFormat.timeZone = TimeZone.getTimeZone("Asia/Jakarta")
                        try {
                            val date: Date? = inputFormat.parse(it)
                            if (date != null) {
                                val formattedDate = outputFormat.format(date)
                                binding.tvDate.text = formattedDate
                                Log.d("FormattedDate", formattedDate)
                            } else {
                                Log.e("DateError", "Parsed date is null")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e("DateError", "Exception during date parsing: ${e.message}")
                        }
                    }
                    if (it?.payload?.statusPesanan == "Selesai" || it?.payload?.statusPesanan == "Dikirim"){
                        binding.btnDeliver.visibility = View.GONE
                    }
                }
        }
    }

    private fun updateTransaksiInvoice() {
        lifecycleScope.launchWhenResumed {
            marketplaceViewModel.pesananDikirim(token, id_transaksi, invoice_id, invoice_url, statusPesanan)
                .observe(this@DetailPesananMasukActivity) { result ->
                    result.onSuccess {
                        Toast.makeText(this@DetailPesananMasukActivity, "Success Update data", Toast.LENGTH_LONG).show()
                        finish()
                    }
                }
        }
    }
}