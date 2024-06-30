package com.capstone.trashtotreasure.view.ui.marketplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityBuyerTransactionBinding
import com.capstone.trashtotreasure.databinding.ActivityPaymentBinding
import com.capstone.trashtotreasure.view.ui.adapter.MyProductsAdapter
import com.capstone.trashtotreasure.view.ui.adapter.MyTransactionPembeliAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class BuyerTransactionActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityBuyerTransactionBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()
    private var token: String = "null"
    private var idPembeli: String = "null"
    private lateinit var myTransactionPembelidapter: MyTransactionPembeliAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.ivNotFound.visibility = View.INVISIBLE
        binding.tvNoData.visibility = View.INVISIBLE
        binding.loadingShimmer.visibility = View.VISIBLE

        marketplaceViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"
            if (token != "null") {
                this.lifecycleScope.launch {
                    marketplaceViewModel.getUser(token!!)
                        .observe(this@BuyerTransactionActivity) {
                            it?.payload?.let {
                                idPembeli = it.idPembeli.toString()
                                loadHomeData()
                                setUpRecyclerView()
                            }
                        }
                }

            }else{
                binding.loadingShimmer.visibility = View.GONE
            }
        }
    }

    private fun loadHomeData() {
        this.lifecycleScope.launch {
            marketplaceViewModel.getTransaksiByPembeli(token!!, idPembeli)
                .observe(this@BuyerTransactionActivity) { response ->
                    binding.loadingShimmer.visibility = View.GONE
                    response?.payload?.let {
                        if (it.isEmpty()) {
                            binding.ivNotFound.visibility = View.VISIBLE
                            binding.rvMyTransaction.visibility = View.GONE
                            Toast.makeText(
                                this@BuyerTransactionActivity,
                                "There is no data to show",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.tvNoData.visibility = View.VISIBLE
                        } else {
                            myTransactionPembelidapter = MyTransactionPembeliAdapter(it)
                            binding.rvMyTransaction.adapter = myTransactionPembelidapter
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
        binding.loadingShimmer.visibility = View.GONE
        val recyclerView = binding.rvMyTransaction
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}