package com.capstone.trashtotreasure.view.ui.home

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityDetailBinding
import com.capstone.trashtotreasure.databinding.ActivityGlassDetailRecommendBinding
import com.capstone.trashtotreasure.view.ui.glass.result.GlassDetailRecommendActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private val binding: ActivityDetailBinding by lazy {
        ActivityDetailBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imageLink = intent.getStringExtra(EXTRA_IMAGE)
        val title = intent.getStringExtra(EXTRA_TITLE)
        val desc = intent.getStringExtra(EXTRA_DESC)
        val url = intent.getStringExtra(EXTRA_URL)


        Glide.with(this)
            .load(imageLink)
            .into(binding.ivBanner)

        binding.tvTitle.text = title
        binding.tvDesc.text = desc

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        binding.btnBaca.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }


    }

    companion object{
        const val EXTRA_TITLE = "EXTRA_TITLE"
        const val EXTRA_DESC = "EXTRA_DESC"
        const val EXTRA_IMAGE = "EXTRA_IMAGE"
        const val EXTRA_URL = "EXTRA_URL"
    }
}