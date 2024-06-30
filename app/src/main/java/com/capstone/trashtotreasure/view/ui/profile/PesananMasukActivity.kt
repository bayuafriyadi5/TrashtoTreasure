package com.capstone.trashtotreasure.view.ui.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityBuyerTransactionBinding
import com.capstone.trashtotreasure.databinding.ActivityPesananMasukBinding
import com.capstone.trashtotreasure.view.ui.adapter.MyTransactionPembeliAdapter
import com.capstone.trashtotreasure.view.ui.adapter.MyTransactionPenjualAdapter
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.capstone.trashtotreasure.view.ui.marketplace.BuyerTransactionActivity
import com.capstone.trashtotreasure.view.ui.marketplace.MarketplaceViewModel
import com.capstone.trashtotreasure.view.ui.marketplace.MyProductActivity
import com.capstone.trashtotreasure.view.ui.marketplace.SellProductActivity
import com.capstone.trashtotreasure.view.ui.marketplace.TransactionActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
@ExperimentalPagingApi
class PesananMasukActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPesananMasukBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()
    private var token: String = "null"
    private var idPenjual: String = "null"
    private var email: String? = null

    private lateinit var myTransactionPenjualdapter: MyTransactionPenjualAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.ivNotFound.visibility = View.INVISIBLE
        binding.tvNoData.visibility = View.INVISIBLE
        binding.loadingShimmer.visibility = View.VISIBLE
        setUpRecyclerView()

        marketplaceViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"
            if (token != "null") {
                this.lifecycleScope.launch {
                    marketplaceViewModel.getUser(token!!)
                        .observe(this@PesananMasukActivity) {
                            it?.payload?.let {
                                email = it.email
                                loadPenjualData(email)
                            }
                        }
                }
            }else{
                binding.loadingShimmer.visibility = View.GONE
            }
        }
    }

    private fun loadPenjualData(email: String?) {
        email?.let {
            this.lifecycleScope.launch {
                val userLiveData = withContext(Dispatchers.IO) { marketplaceViewModel.getPenjualByEmail(token, it) }
                userLiveData.observe(this@PesananMasukActivity) { profile ->
                    if (profile != null && this@PesananMasukActivity.lifecycle.currentState.isAtLeast(
                            Lifecycle.State.STARTED)) {
                        val emailPenjual =profile.payload?.email
                        if (emailPenjual != null) {
                            idPenjual = profile.payload.idPenjual.toString()
                            loadHomeData(idPenjual)
                        }
                    } else {
                        Toast.makeText(
                           this@PesananMasukActivity,
                            "Gagal Memuat Data Penjual",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }
    private fun loadHomeData(idPenjual: String) {
        this.lifecycleScope.launch {
            marketplaceViewModel.getTransaksiByPenjual(token!!, idPenjual)
                .observe(this@PesananMasukActivity) {
                    binding.loadingShimmer.visibility = View.GONE
                    it?.payload?.let {
                        if (it.isEmpty()) {
                            binding.loadingShimmer.visibility = View.GONE
                            binding.ivNotFound.visibility = View.VISIBLE
                            binding.rvMyTransaction.visibility = View.GONE
                            Toast.makeText(
                                this@PesananMasukActivity,
                                "There is no data to show",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            myTransactionPenjualdapter = MyTransactionPenjualAdapter(it)
                            binding.rvMyTransaction.adapter = myTransactionPenjualdapter
                            binding.ivNotFound.visibility = View.GONE
                            binding.rvMyTransaction.visibility = View.VISIBLE
                        }
                    } ?: run {
                        binding.ivNotFound.visibility = View.VISIBLE
                        binding.rvMyTransaction.visibility = View.GONE
                    }
                }
        }
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.rvMyTransaction
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}