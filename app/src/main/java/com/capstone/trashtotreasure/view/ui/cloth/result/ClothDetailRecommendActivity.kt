package com.capstone.trashtotreasure.view.ui.cloth.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityClothDetailRecommendBinding
import com.capstone.trashtotreasure.databinding.ActivityGlassDetailRecommendBinding
import com.capstone.trashtotreasure.model.data.local.DetailRecommend
import com.capstone.trashtotreasure.view.ui.glass.result.GlassDetailRecommendActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class ClothDetailRecommendActivity : AppCompatActivity() {

    private val binding: ActivityClothDetailRecommendBinding by lazy {
        ActivityClothDetailRecommendBinding.inflate(layoutInflater)
    }

    lateinit var craft: DetailRecommend.Craft

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val imageLink = intent.getStringExtra(EXTRA_TITLE)

        val decodedLink = URLDecoder.decode(imageLink, "UTF-8") // Decode the link
        val fileName = decodedLink?.substringAfterLast("/") // Get the file name from the link
        val word = fileName?.substringBeforeLast(".") // Remove the file extension

        var result = word

        Glide.with(this)
            .load(imageLink)
            .into(binding.ivBanner)

        binding.tvTitle.text = result

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        if(result == "Apron"){
            if (DetailRecommend.cloth.isNotEmpty()) {
                craft = DetailRecommend.cloth[0]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("UxLhtzSiHSo", 0f)
                }
            })
        }else if(result == "Boneka"){
            if (DetailRecommend.cloth.isNotEmpty()) {
                craft = DetailRecommend.cloth[1]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("Of4lUd9c8XI", 0f)
                }
            })
        }else if(result == "Bros"){
            if (DetailRecommend.cloth.isNotEmpty()) {
                craft = DetailRecommend.cloth[2]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("f0QnESMgwlc", 0f)
                }
            })
        }else if(result == "Bunga"){
            if (DetailRecommend.cloth.isNotEmpty()) {
                craft = DetailRecommend.cloth[3]
            }
            binding.tvDesc.text = craft.description
            binding.tvTitle.text = craft.name
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("oGCzOP8ppjs", 0f)
                }
            })
        }else if(result == "Dompet"){
            if (DetailRecommend.cloth.isNotEmpty()) {
                craft = DetailRecommend.cloth[4]
            }
            binding.tvDesc.text = craft.description
            binding.tvTitle.text = craft.name
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("lI--g3u_fAk", 0f)
                }
            })
        }else if(result == "Gorden"){
            if (DetailRecommend.cloth.isNotEmpty()) {
                craft = DetailRecommend.cloth[5]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("SRPItg1KcWM", 0f)
                }
            })
        }else if(result == "Hiasan Dinding"){
            if (DetailRecommend.cloth.isNotEmpty()) {
                craft = DetailRecommend.cloth[6]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("tvL4drZAs_Y", 0f)
                }
            })
        }else if(result == "Jepit Rambut"){
            if (DetailRecommend.cloth.isNotEmpty()) {
                craft = DetailRecommend.cloth[7]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("3CHnlvITClE", 0f)
                }
            })
        }
    }

    companion object{
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }
}