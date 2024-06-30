package com.capstone.trashtotreasure.view.ui.marketplace

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityMyProductBinding
import com.capstone.trashtotreasure.view.ui.adapter.MyProductsAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class MyProductActivity : AppCompatActivity() {

    private val binding: ActivityMyProductBinding by lazy {
        ActivityMyProductBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()
    private lateinit var myProductsAdapter: MyProductsAdapter
    private var token: String? = null
    private var id_penjual: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        id_penjual = intent.getStringExtra("id_penjual")
        binding.ivNotFound.visibility = View.INVISIBLE
        binding.tvNoData.visibility = View.INVISIBLE
        binding.loadingShimmer.visibility = View.VISIBLE
        binding.rvMyProduct.visibility = View.GONE
        setUpRecyclerView()

        marketplaceViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token
            if (token != null && id_penjual != null) {
                loadHomeData()
            } else {
                binding.loadingShimmer.stopShimmer()
                binding.loadingShimmer.visibility = View.GONE
                showEmptyState("Token or ID Penjual is missing")
            }
        }
    }

    private fun loadHomeData() {
        lifecycleScope.launch {
            try {
                marketplaceViewModel.getProductByPenjual(token!!, id_penjual!!)
                    .observe(this@MyProductActivity) { response ->
                        binding.loadingShimmer.stopShimmer()
                        binding.loadingShimmer.visibility = View.GONE

                        if (response?.payload != null) {
                            if (response.payload.isNotEmpty()) {
                                Log.d("MyProductActivity", "Product list loaded with ${response.payload.size} items")
                                myProductsAdapter = MyProductsAdapter(response.payload)
                                binding.rvMyProduct.adapter = myProductsAdapter
                                binding.ivNotFound.visibility = View.GONE
                                binding.rvMyProduct.visibility = View.VISIBLE
                            } else {
                                Log.d("MyProductActivity", "Product list is empty")
                                showEmptyState("There is no product to show")
                            }
                        } else {
                            Log.d("MyProductActivity", "Response is null or payload is null")
                            showEmptyState("Gagal memuat data produk")
                        }
                    }
            } catch (e: Exception) {
                binding.loadingShimmer.stopShimmer()
                binding.loadingShimmer.visibility = View.GONE
                Log.e("MyProductActivity", "Error loading data", e)
                showEmptyState("Gagal memuat data produk: ${e.message}")
                Toast.makeText(this@MyProductActivity, "no data", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showEmptyState(message: String) {
        binding.tvNoData.visibility = View.VISIBLE
        binding.ivNotFound.visibility = View.VISIBLE
        binding.rvMyProduct.visibility = View.GONE
        Toast.makeText(this@MyProductActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.rvMyProduct
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}



