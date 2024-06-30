package com.capstone.trashtotreasure.view.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.databinding.ItemMyProductBinding
import com.capstone.trashtotreasure.databinding.ItemProductBinding
import com.capstone.trashtotreasure.model.data.remote.response.getProductByPenjual.PayloadItem
import com.capstone.trashtotreasure.view.ui.marketplace.RestokActivity
import com.capstone.trashtotreasure.view.ui.marketplace.TransactionActivity

@ExperimentalPagingApi
class MyProductsAdapter (private val productsList: List<PayloadItem?>) :
    RecyclerView.Adapter<MyProductsAdapter.AdsViewHolder>()  {

    inner class AdsViewHolder(private val binding: ItemMyProductBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myProductsItem: PayloadItem) {

            Glide.with(binding.root.context)
                .load(myProductsItem.fotoProduk)
                .into(binding.ivProduct)

            binding.tvProductName.text = myProductsItem.namaProduk
            binding.tvHarga.text = "Rp. "+myProductsItem.hargaProduk.toString()
            binding.tvStok.text = "Stock: "+myProductsItem.stokProduk.toString()

            binding.root.setOnClickListener {
                val context = binding.root.context

                Log.d("ProductsAdapter", "Product ID: ${myProductsItem.idProduk}, Product Price: ${myProductsItem.hargaProduk}")

                if (myProductsItem.idProduk != null && myProductsItem.hargaProduk != null) {
                    val intent = Intent(context, RestokActivity::class.java).apply {
                        putExtra("id_produk", myProductsItem.idProduk.toString())
                    }
                    context.startActivity(intent)
                } else {
                    Log.e("ProductsAdapter", "Product ID or Price is null")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding = ItemMyProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        productsList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount() = productsList.size
}