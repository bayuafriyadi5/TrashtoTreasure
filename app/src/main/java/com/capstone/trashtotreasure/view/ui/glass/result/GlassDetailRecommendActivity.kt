package com.capstone.trashtotreasure.view.ui.glass.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityGlassDetailRecommendBinding
import com.capstone.trashtotreasure.databinding.ActivityPlasticDetailRecommendBinding
import com.capstone.trashtotreasure.model.data.local.DetailRecommend
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder

@AndroidEntryPoint
class GlassDetailRecommendActivity : AppCompatActivity() {

    private val binding: ActivityGlassDetailRecommendBinding by lazy {
        ActivityGlassDetailRecommendBinding.inflate(layoutInflater)
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

        if(result == "Aquasqape"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[0]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("dYjdwRlslnI", 0f)
                }
            })
        }else if(result == "Lampu Hias"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[1]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("4XkwcIp-E3s", 0f)
                }
            })
        }else if(result == "Lonceng kaca"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[2]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("LRuMzY-MHRg", 0f)
                }
            })
        }else if(result == "Pajangan"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[3]
            }
            binding.tvDesc.text = craft.description
            binding.tvTitle.text = craft.name
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("Y8-Zy0sBm-s", 0f)
                }
            })
        }else if(result == "Tempat Penyimpanan"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[4]
            }
            binding.tvDesc.text = craft.description
            binding.tvTitle.text = craft.name
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("eZOmIVbT2QQ", 0f)
                }
            })
        }else if(result == "Vas"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[5]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("xZwa9YJCrmY", 0f)
                }
            })
        }else if(result == "Frame foto"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[6]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("qMQ6KyvVqUE", 0f)
                }
            })
        }else if(result == "Hiasan Cermin"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[7]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("GpfIXHoAjQo", 0f)
                }
            })
        }else if(result == "Hiasan Dinding"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[8]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("Ja_JBiYsydI", 0f)
                }
            })
        }else if(result == "Menara Kaca"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[9]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("8TFpC5oFqno", 0f)
                }
            })
        }else if(result == "Mozaik"){
            if (DetailRecommend.glass.isNotEmpty()) {
                craft = DetailRecommend.glass[10]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("6RKNutq5FWg", 0f)
                }
            })
        }
    }


    companion object{
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }
}