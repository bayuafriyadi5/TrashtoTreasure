package com.capstone.trashtotreasure.view.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ItemArticleBinding
import com.capstone.trashtotreasure.model.data.local.entitiy.NewsEntity
import com.capstone.trashtotreasure.view.ui.adapter.ArticleAdapter.MyViewHolder
import java.util.*

class ArticleAdapter(private val onBookmarkClick:  (NewsEntity) -> Unit) : PagingDataAdapter<NewsEntity, MyViewHolder>(DIFF_CALLBACK) {

    private var lastPosition = RecyclerView.NO_POSITION
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        if (news != null) {
            holder.bind(news)
        }

        val ivBookmark = holder.binding.ivBookmark
        if (news?.isBookmarked == true) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmarked_24))
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmark_border_24))
        }
        ivBookmark.setOnClickListener {
            if (news != null) {
                onBookmarkClick(news)
                // Update the isBookmarked property manually
                news.isBookmarked = !news.isBookmarked
                // Notify the adapter of the data change
                notifyItemChanged(position)
            }
        }
    }

    override fun onViewDetachedFromWindow(holder: MyViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder.bindingAdapterPosition == itemCount - 1) {
            lastPosition = itemCount - 1
        }
    }

    override fun onViewAttachedToWindow(holder: MyViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder.bindingAdapterPosition == lastPosition) {
            holder.itemView.post {
                holder.itemView.requestFocus()
                holder.itemView.requestFocusFromTouch()
            }
        }
    }


    class MyViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(
        binding.root
    ){
        @SuppressLint("SimpleDateFormat")
        fun bind(news: NewsEntity?) {
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
//            dateFormat.timeZone = TimeZone.getTimeZone("ID")
//            val time = dateFormat.parse(news.publishedAt)?.time
//            val prettyTime = PrettyTime(Locale.getDefault())
//            val date = prettyTime.format(time?.let { Date(it) })

            binding.tvItemTitle.text = news?.title
//            binding.tvItemPublishedDate.text = date


            Glide.with(itemView.context)
                .load(news?.imageUrl)
                .into(binding.imgPoster)

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(news?.url)
                itemView.context.startActivity(intent)
            }
        }
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NewsEntity>() {
            override fun areItemsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem.title == newItem.title
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: NewsEntity, newItem: NewsEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}