package com.capstone.trashtotreasure.view.ui.marketplace

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityPaymentBinding
import com.capstone.trashtotreasure.databinding.ActivityTransactionBinding
import com.capstone.trashtotreasure.view.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

@AndroidEntryPoint
@ExperimentalPagingApi
class PaymentActivity: AppCompatActivity() {

    private val binding by lazy {
        ActivityPaymentBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()

    private var token: String = "null"
    private var invoice_id: String = "null"
    private var invoice_url: String = "null"
    private var id_transaksi: String = "null"
    private var statusPesanan: String = "null"
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        invoice_id = intent.getStringExtra("invoice_id") ?: "null"
        invoice_url = intent.getStringExtra("invoice_url") ?: "null"
        id_transaksi = intent.getStringExtra("id_transaksi") ?: "no_id"

        marketplaceViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"
            if (token != null) {

                this.lifecycleScope.launch {
                    marketplaceViewModel.getTransaksiById(token!!, id_transaksi)
                        .observe(this@PaymentActivity) {
                            if (it?.payload?.status == "unpaid"){
                                updateTransaksiInvoice()
                                loadWebView()
                            }else{
                                loadWebView()
                            }
                        }
                }


            }
        }

        webView = binding.webView
        configureWebView()

        binding.toolbarBack.setOnClickListener {
            finish()
        }

        binding.refreshPage.setOnRefreshListener { refreshPage() }
    }

    private fun configureWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()  // You might need to customize this
    }

    private fun refreshPage() {
        binding.refreshPage.isRefreshing = true
        Timer().schedule(1000) {
            binding.refreshPage.isRefreshing = false
        }
        loadWebView()
    }

    private fun loadWebView() {
        if (invoice_url != "null") {
            webView.loadUrl(invoice_url)
        } else {
            Toast.makeText(this, "Invalid Invoice URL", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateTransaksiInvoice() {
        lifecycleScope.launchWhenResumed {
            marketplaceViewModel.updateTransaksiInvoice(token, id_transaksi, invoice_id, invoice_url, statusPesanan)
                .observe(this@PaymentActivity) { result ->
                    result.onSuccess {
                        Toast.makeText(this@PaymentActivity, "Success get data", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
}
