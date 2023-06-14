package com.capstone.trashtotreasure.view.ui.metal.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityPlasticDetailRecommendBinding
import com.capstone.trashtotreasure.model.data.local.DetailRecommend
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class MetalDetailRecommendActivity : AppCompatActivity() {

    private val binding: ActivityPlasticDetailRecommendBinding by lazy {
        ActivityPlasticDetailRecommendBinding.inflate(layoutInflater)
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

        if(result == "Bunga"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[0]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("pcJvq1Vf7aw", 0f)
                }
            })
        }else if(result == "Celengan"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[1]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("LRFVr6ty43g", 0f)
                }
            })
        }else if(result == "Hiasan Dinding"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[2]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("6xcBKnjHnPU", 0f)
                }
            })
        }else if(result == "Kompor"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[3]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("VSnTX87FlYQ", 0f)
                }
            })
        }else if(result == "Lampu"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[4]
            }
            binding.tvDesc.text = craft.description
            binding.tvTitle.text = craft.name
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("YxM0ga8LlMw", 0f)
                }
            })
        }else if(result == "Mainan"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[5]
            }
            binding.tvDesc.text = craft.description
            binding.tvTitle.text = craft.name
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("jp89-mqaGtc", 0f)
                }
            })
        }else if(result == "Pot Tanaman"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[6]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("bkXLpm4fFkY", 0f)
                }
            })
        }else if(result == "Tempat Penyimpanan"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[7]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("NEt07U4YRqo", 0f)
                }
            })
        }else if(result == "Hiasan Lampu"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[8]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("RM2thAoNEso", 0f)
                }
            })
        }else if(result == "Kerajinan Bunga"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[9]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("IwONhXPoU14", 0f)
                }
            })
        }else if(result == "Miniatur Patung"){
            if (DetailRecommend.metal.isNotEmpty()) {
                craft = DetailRecommend.metal[10]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("jfKzmiiay3E", 0f)
                }
            })
        }
    }


    companion object{
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }
}