package com.capstone.trashtotreasure.view.ui.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ItemArticleBinding
import com.capstone.trashtotreasure.model.data.local.entitiy.ArticleEntity
import com.capstone.trashtotreasure.view.ui.adapter.ArticleAdapter.MyViewHolder
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*

class ArticleAdapter(private val onBookmarkClick: (ArticleEntity) -> Unit) : ListAdapter<ArticleEntity, MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val news = getItem(position)
        holder.bind(news)

        val ivBookmark = holder.binding.ivBookmark
        if (news.isBookmarked) {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmarked_24))
        } else {
            ivBookmark.setImageDrawable(ContextCompat.getDrawable(ivBookmark.context, R.drawable.ic_bookmark_border_24))
        }
        ivBookmark.setOnClickListener {
            onBookmarkClick(news)
        }
    }

    class MyViewHolder(val binding: ItemArticleBinding) : RecyclerView.ViewHolder(
        binding.root
    ){
        @SuppressLint("SimpleDateFormat")
        fun bind(news: ArticleEntity) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            dateFormat.timeZone = TimeZone.getTimeZone("ID")
            val time = dateFormat.parse(news.publishedAt)?.time
            val prettyTime = PrettyTime(Locale.getDefault())
            val date = prettyTime.format(time?.let { Date(it) })

            binding.tvItemTitle.text = news.title
            binding.tvItemPublishedDate.text = date


            Glide.with(itemView.context)
                .load(news.urlToImage)
                .into(binding.imgPoster)

            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(news.url)
                itemView.context.startActivity(intent)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<ArticleEntity>) {
        submitList(data)
        notifyDataSetChanged()
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArticleEntity> =
            object : DiffUtil.ItemCallback<ArticleEntity>() {
                override fun areItemsTheSame(oldUser: ArticleEntity, newUser: ArticleEntity): Boolean {
                    return oldUser.title == newUser.title
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: ArticleEntity, newUser: ArticleEntity): Boolean {
                    return oldUser == newUser
                }
            }
    }
}