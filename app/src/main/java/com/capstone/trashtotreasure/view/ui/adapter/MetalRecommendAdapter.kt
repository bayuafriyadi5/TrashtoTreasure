package com.capstone.trashtotreasure.view.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.databinding.ItemRecommendBinding
import com.capstone.trashtotreasure.view.ui.metal.result.MetalDetailRecommendActivity


class MetalRecommendAdapter(private val list: List<MetalRecommendationItem>) :
    RecyclerView.Adapter<MetalRecommendAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.bind(data)

        holder.binding.cardRecommend.setOnClickListener {
            val intent = Intent(holder.itemView.context, MetalDetailRecommendActivity::class.java)
            intent.putExtra(MetalDetailRecommendActivity.EXTRA_TITLE, data.imageUrl)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount() = list.size

    class ViewHolder(val binding: ItemRecommendBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recommend: MetalRecommendationItem) {

            Glide.with(itemView)
                .load(recommend.imageUrl)
                .into(binding.imgPoster)
        }
    }
}

data class MetalRecommendationItem(val imageUrl: String)