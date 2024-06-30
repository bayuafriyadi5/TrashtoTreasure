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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityEditProfileBinding
import com.capstone.trashtotreasure.databinding.ActivitySellProductBinding
import com.capstone.trashtotreasure.utils.fromUriToFile
import com.capstone.trashtotreasure.utils.reduceFileImage
import com.capstone.trashtotreasure.view.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@AndroidEntryPoint
@ExperimentalPagingApi
class SellProductActivity : AppCompatActivity() {

    private val binding: ActivitySellProductBinding by lazy {
        ActivitySellProductBinding.inflate(layoutInflater)
    }

    private val marketplaceViewModel: MarketplaceViewModel by viewModels()

    private var token: String = "null"
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.loading.visibility = View.INVISIBLE

        marketplaceViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"
        }

        binding.addPhoto.setOnClickListener { startGallery() }

        binding.addProductButton.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            val nama = binding.namaProductEditText.text.toString()
            val price = binding.priceEditText.text.toString()
            val stok = binding.stokEditText.text.toString()
            val desc = binding.descEditText.text.toString()


            when {
                nama.isEmpty() -> {
                    binding.textInputProductName.error = "Silahkan Masukan Nama Produk"
                    binding.loading.visibility = View.INVISIBLE
                }
                price.isEmpty() -> {
                    binding.textInputPrice.error = "Silahkan Masukan Harga Produk"
                    binding.loading.visibility = View.INVISIBLE
                }
                desc.length < 8 -> {
                    Toast.makeText(
                        this,
                        "Deskripsi harus jelas dan menggambarkan produk dengan baik",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.loading.visibility = View.INVISIBLE
                }
                desc.isEmpty() -> {
                    binding.textInputDesc.error = "Silahkan Masukan Desckripsi Produk Anda"
                    binding.loading.visibility = View.INVISIBLE
                }
                stok.isEmpty() -> {
                    binding.textInputStock.error = "Silahkan Masukan Jumlah Stok Produk Anda"
                    binding.loading.visibility = View.INVISIBLE
                }
                else -> {
                    lifecycleScope.launchWhenResumed {
                        launch {
                            if (getFile != null) {
                                val file = reduceFileImage(getFile as File)
                                marketplaceViewModel.addProduct(token, nama, price, stok, desc, file)
                                    .observe(this@SellProductActivity) { result ->
                                        handleUpdateResult(result)
                                        binding.loading.visibility = View.INVISIBLE
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
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
            AlertDialog.Builder(this@SellProductActivity).apply {
                setTitle("Yeah!")
                setMessage("Your product has been successfully added")
                setPositiveButton("Next") { _, _ -> finish() }
                create()
                show()
            }
        }
        result.onFailure {
            val error = it.message
            Log.e("SellProductActivity", "handleUpdateResult failure: $error")
            Toast.makeText(this@SellProductActivity, error, Toast.LENGTH_SHORT).show()
            binding.loading.visibility = View.INVISIBLE
        }
    }

}