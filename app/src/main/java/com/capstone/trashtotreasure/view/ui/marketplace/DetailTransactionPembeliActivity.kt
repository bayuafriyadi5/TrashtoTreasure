package com.capstone.trashtotreasure.view.ui.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.circleCrop
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityBuyerTransactionBinding
import com.capstone.trashtotreasure.databinding.ActivityDetailTransactionPembeliBinding
import com.capstone.trashtotreasure.view.ui.adapter.MyTransactionPembeliAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@AndroidEntryPoint
@ExperimentalPagingApi

class DetailTransactionPembeliActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetailTransactionPembeliBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()
    private var token: String = "null"
    private var idPembeli: String = "null"
    private var nama: String = "null"
    private var invoice_url: String = "null"
    private var invoice_id: String = "null"
    private var id_transaksi: String = "null"
    private var statusPesanan: String = "Selesai"

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
                        .observe(this@DetailTransactionPembeliActivity) {
                            it?.payload?.let {
                                idPembeli = it.idPembeli.toString()
                                nama = it.nama.toString()
                                binding.loading.visibility = View.GONE
                                loadHomeData()
                                binding.loadingShimmerProfile.visibility = View.INVISIBLE
                            }
                        }
                }
            }
        }

        binding.btnPayment.setOnClickListener {
            val intent = Intent(this@DetailTransactionPembeliActivity, PaymentActivity::class.java)
            intent.putExtra("invoice_url", invoice_url)
            intent.putExtra("invoice_id", invoice_id)
            intent.putExtra("id_transaksi", id_transaksi)
            startActivity(intent)
        }

        binding.btnUpdate.setOnClickListener {
            updateTransaksiInvoice()
        }
    }

    private fun updateTransaksiInvoice() {
        lifecycleScope.launchWhenResumed {
            marketplaceViewModel.transaksiSelesai(token, id_transaksi, invoice_id, invoice_url, statusPesanan)
                .observe(this@DetailTransactionPembeliActivity) { result ->
                    result.onSuccess {
                        Toast.makeText(this@DetailTransactionPembeliActivity, "Success Update data", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }


    private fun loadHomeData() {
        this.lifecycleScope.launch {
            marketplaceViewModel.getTransaksiById(token!!, id_transaksi)
                .observe(this@DetailTransactionPembeliActivity) {
                    // Log the entire response to inspect the structure
                    Log.d("TransactionResponse", it.toString())

                    Glide.with(this@DetailTransactionPembeliActivity)
                        .load(it?.payload?.produk?.fotoProduk)
                        .into(binding.ivProduct)

                    binding.tvName.visibility = View.VISIBLE
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
                    binding.btnPayment.visibility = View.VISIBLE
                    binding.ivProduct.visibility = View.VISIBLE
                    binding.btnUpdate.visibility = View.VISIBLE

                    binding.tvNamaProduct.text = it?.payload?.produk?.namaProduk
                    binding.tvDescProduct.text = it?.payload?.produk?.descProduk
                    binding.tvAddressDetail.text = it?.payload?.alamat ?: "N/A"
                    binding.tvProductPriceSummary.text = it?.payload?.produk?.hargaProduk?.toString()
                    binding.tvQty.text = it?.payload?.qty?.toString()
                    binding.tvTotalPrice.text = it?.payload?.totalHarga
                    binding.tvStatusPesanan.text = it?.payload?.statusPesanan
                    binding.tvDate.text = it?.payload?.updatedAt
                    binding.tvName.text = nama
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

                   if (it?.payload?.statusPesanan == "Sampai" || it?.payload?.statusPesanan == "Disiapkan"){
                       binding.btnPayment.setText("Invoice")
                       binding.btnUpdate.visibility = View.GONE
                   }
                }
        }
    }
}