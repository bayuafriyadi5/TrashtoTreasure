package com.capstone.trashtotreasure.view.ui.adapter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.databinding.ItemMyTransactionBinding
import com.capstone.trashtotreasure.model.data.remote.response.getTransaksiByPenjual.PayloadItem
import com.capstone.trashtotreasure.view.ui.marketplace.DetailTransactionPembeliActivity
import com.capstone.trashtotreasure.view.ui.profile.DetailPesananMasukActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone



@ExperimentalPagingApi
class MyTransactionPenjualAdapter (private val productsList: List<PayloadItem?>) :
    RecyclerView.Adapter<MyTransactionPenjualAdapter.AdsViewHolder>()  {

    inner class AdsViewHolder(private val binding: ItemMyTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(myTransactionItem: PayloadItem) {

            myTransactionItem.produk?.let { produk ->
                Glide.with(binding.root.context)
                    .load(produk.fotoProduk)
                    .into(binding.ivProduct)

                binding.tvProductName.text = produk.namaProduk
                binding.tvHarga.text = "Rp "+ produk.hargaProduk.toString()
            }

            if (myTransactionItem.status == "unpaid"){
                binding.tvStatusPesanan.text = "Menunggu Pembayaran"
            }else{
                binding.tvStatusPesanan.text = myTransactionItem.statusPesanan.toString()
            }
            // Format the total harga
            binding.tvHarga.text = myTransactionItem.totalHarga.toString()

            myTransactionItem.updatedAt?.let {
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

            binding.root.setOnClickListener {
                val context = binding.root.context

                // Add debug logs
                Log.d("MyTransactionPembeliAdapter", "Transaction ID: ${myTransactionItem.idTransaksi}}")

                // Check if idProduk and hargaProduk are null
                if (myTransactionItem.idTransaksi != null) {
                    val intent = Intent(context, DetailPesananMasukActivity::class.java).apply {
                        putExtra("id_transaksi", myTransactionItem.idTransaksi.toString())
                        putExtra("invoice_id", myTransactionItem.invoiceId.toString())
                        putExtra("invoice_url", myTransactionItem.invoiceUrl.toString())
                    }
                    context.startActivity(intent)
                } else {
                    Log.e("MyTransactionPembeliAdapter", "Transaction ID is null")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdsViewHolder {
        val binding = ItemMyTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdsViewHolder, position: Int) {
        productsList[position]?.let { holder.bind(it) }
    }

    override fun getItemCount() = productsList.size
}