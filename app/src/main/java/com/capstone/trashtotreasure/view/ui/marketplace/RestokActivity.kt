package com.capstone.trashtotreasure.view.ui.marketplace

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityRestokBinding
import com.capstone.trashtotreasure.databinding.ActivityTransactionBinding
import com.capstone.trashtotreasure.utils.fromUriToFile
import com.capstone.trashtotreasure.utils.reduceFileImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
@ExperimentalPagingApi
class RestokActivity : AppCompatActivity() {

    private val binding: ActivityRestokBinding by lazy {
        ActivityRestokBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()

    private var token: String = "null"
    private var idProduk: String = "null"
    private var getFile: File? = null

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
                        .observe(this@RestokActivity) {
                            it?.payload?.let {
                                loadData()
                            }
                        }
                }
            }
        }

        binding.addPhoto.setOnClickListener { startGallery() }

        binding.updateProductButton.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            val nama = binding.namaProductEditText.text.toString()
            val desc = binding.descEditText.text.toString()
            val harga = binding.priceEditText.text.toString()
            val stok = binding.stokEditText.text.toString()

            lifecycleScope.launchWhenResumed {
                launch {
                    if (getFile != null) {
                        val file = reduceFileImage(getFile as File)
                        marketplaceViewModel.updateProduct(
                            token,
                            idProduk,
                            nama,
                            harga,
                            stok,
                            desc,
                            file
                        )
                            .observe(this@RestokActivity) { result ->
                                handleUpdateResult(result)
                            }
                    } else {
                        marketplaceViewModel.updateProdukNoImage(token, idProduk, nama, desc, harga, stok)
                            .observe(this@RestokActivity) { result ->
                                handleUpdateResult(result)
                            }
                    }
                }
            }
        }
    }

        private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data as Uri

            selectedImg.let { uri ->
                val myFile = fromUriToFile(uri, this)
                getFile = myFile
                Glide.with(this)
                    .load(uri)
                    .into(binding.ivProfile)
            }
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun handleUpdateResult(result: Result<Any>) {
        result.onSuccess {
            binding.loading.visibility = View.INVISIBLE
            AlertDialog.Builder(this).apply {
                setTitle("Success")
                setMessage("Your product has been successfully updated")
                setPositiveButton("Next") { _, _ -> finish() }
                create()
                show()
            }
        }
        result.onFailure {
            val error = it.message
            Toast.makeText(this@RestokActivity, error, Toast.LENGTH_SHORT).show()
            binding.loading.visibility = View.INVISIBLE
            Log.e("RestokActivity", "Update failed: ${it.stackTraceToString()}")
        }
    }


    private fun loadData() {
        this.lifecycleScope.launch {
            marketplaceViewModel.getProductById(token, idProduk)
                .observe(this@RestokActivity) { response ->
                    response?.payload?.let {
                        binding.loading.visibility = View.INVISIBLE
                        binding.namaProductEditText.setText(it.namaProduk)
                        binding.descEditText.setText(it.descProduk)
                        binding.priceEditText.setText(it.hargaProduk?.toString() ?: "")
                        binding.stokEditText.setText(it.stokProduk?.toString() ?: "")
                        Glide.with(this@RestokActivity)
                            .load(it.fotoProduk)
                            .into(binding.ivProfile)
                    }
                }
        }
    }

}