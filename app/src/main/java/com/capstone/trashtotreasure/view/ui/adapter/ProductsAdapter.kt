package com.capstone.trashtotreasure.view.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.databinding.ItemProductBinding
import com.capstone.trashtotreasure.model.data.remote.response.product.PayloadItem
import com.capstone.trashtotreasure.view.ui.marketplace.TransactionActivity
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalPagingApi
class ProductsAdapter(private var productsList: List<PayloadItem?>) :
    RecyclerView.Adapter<ProductsAdapter.AdsViewHolder>() {

    inner class AdsViewHolder(private val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(productsItem: PayloadItem) {
            Glide.with(binding.root.context)
                .load(productsItem.fotoProduk)
                .into(binding.ivProduct)

            binding.tvProductName.text = productsItem.namaProduk
            binding.tvHarga.text = "Rp. " + productsItem.hargaProduk.toString()

            binding.root.setOnClickListener {
                val context = binding.root.context

                Log.d("ProductsAdapter", "Product ID: ${productsItem.idProduk}, Product Price: ${productsItem.hargaProduk}")

                if (productsItem.idProduk != null && productsItem.hargaProduk != null) {
                    val intent = Intent(context, TransactionActivity::class.java).apply {
                        putExtra("id_produk", productsItem.idProduk.toString())
                        putExtra("nama_produk", productsItem.namaProduk)
                    }
                    context.startActivity(intent)
                } else {
                    Log.e("ProductsAdapter", "Product ID or Price is null")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        productsList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount() = productsList.size

    fun updateData(newProductList: List<PayloadItem?>) {
        productsList = newProductList
        notifyDataSetChanged()
    }
}

