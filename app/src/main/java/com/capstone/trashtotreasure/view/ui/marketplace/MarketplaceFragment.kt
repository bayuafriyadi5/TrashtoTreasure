package com.capstone.trashtotreasure.view.ui.marketplace

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.trashtotreasure.databinding.FragmentMarketplaceBinding
import com.capstone.trashtotreasure.view.ui.adapter.ProductsAdapter
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.core.widget.addTextChangedListener
import com.capstone.trashtotreasure.utils.NetworkUtil
import com.capstone.trashtotreasure.view.ui.adapter.ProductsNameAdapter

@AndroidEntryPoint
@ExperimentalPagingApi
class MarketplaceFragment : Fragment() {

    private var _binding: FragmentMarketplaceBinding? = null
    private val marketplaceViewModel: MarketplaceViewModel by viewModels()
    private val binding get() = _binding!!
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var productsSearchAdapter: ProductsNameAdapter
    private var token: String = "null"
    private var email: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarketplaceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkNetworkAndSession()
        setUpRecyclerView()

        marketplaceViewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) { token ->
            this.token = token ?: "null"
            if (token != "null") {
                loadHomeData()
            }
        }

        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (query.isNotEmpty()) {
                    searchProductByName(query)
                } else {
                    binding.rvProductList.adapter = productsAdapter
                }
            }
        })
    }

    private fun searchProductByName(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            marketplaceViewModel.getProductByName(token, query).observe(viewLifecycleOwner) { response ->
                response?.payload?.let { product ->
                    val productList = listOf(product) // Convert single product to list
                    productsSearchAdapter.submitList(productList) // Update the adapter with new data
                    binding.rvProductList.adapter = productsSearchAdapter
                }
            }
        }
    }

    private fun checkNetworkAndSession() {
        if (!NetworkUtil.isNetworkAvailable(requireContext())) {
            showNetworkError()
        } else {
            checkSession()
        }
    }

    private fun showNetworkError() {
        Toast.makeText(requireContext(), "No internet connection...", Toast.LENGTH_LONG).show()
        // Optionally, you can direct the user to a no internet activity or show a retry button
    }

    override fun onResume() {
        super.onResume()
        if (token != "null") {
            loadHomeData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.rvProductList
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        productsAdapter = ProductsAdapter(listOf())
        productsSearchAdapter = ProductsNameAdapter()
        recyclerView.adapter = productsAdapter
    }

    private fun loadHomeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            marketplaceViewModel.getAllProducts(token!!).observe(viewLifecycleOwner) { adsResponse ->
                adsResponse?.payload?.let { adsList ->
                    binding.loadingShimmerProfile.visibility = View.INVISIBLE
                    binding.rvProductList.visibility = View.VISIBLE
                    productsAdapter.updateData(adsList)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            marketplaceViewModel.getUser(token!!).observe(viewLifecycleOwner) {
                it?.payload?.let {
                    email = it.email
                    loadPenjualData(email)
                }
            }
        }
    }

    private fun loadPenjualData(email: String?) {
        email?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                val userLiveData = withContext(Dispatchers.IO) { marketplaceViewModel.getPenjualByEmail(token, it) }
                userLiveData.observe(viewLifecycleOwner) { profile ->
                    if (profile != null && viewLifecycleOwner.lifecycle.currentState.isAtLeast(
                            Lifecycle.State.STARTED)) {
                        val emailPenjual = profile.payload?.email
                        if (emailPenjual != null){
                            binding.btnSell.setOnClickListener {
                                val intent = Intent(requireContext(), SellProductActivity::class.java)
                                startActivity(intent)
                            }
                            binding.btnMyProduct.setOnClickListener {
                                val id_penjual = profile.payload.idPenjual.toString()
                                val intent = Intent(requireContext(), MyProductActivity::class.java)
                                intent.putExtra("id_penjual", id_penjual)
                                startActivity(intent)
                            }
                            binding.btnTransaction.setOnClickListener {
                                val intent = Intent(requireContext(), BuyerTransactionActivity::class.java)
                                startActivity(intent)
                            }
                        } else {
                            binding.btnSell.setOnClickListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Kamu belum mendaftar sebagai penjual",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            binding.btnMyProduct.setOnClickListener {
                                Toast.makeText(
                                    requireContext(),
                                    "Kamu belum mendaftar sebagai penjual",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            binding.btnTransaction.setOnClickListener {
                                val intent = Intent(requireContext(), BuyerTransactionActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun checkSession() {
        marketplaceViewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) {
            if (it == "null") {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }
}

