package com.capstone.trashtotreasure.view.ui.plastic.result

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityPlasticDetailRecommendBinding
import com.capstone.trashtotreasure.databinding.ActivityPlasticResultBinding
import com.capstone.trashtotreasure.model.data.local.DetailRecommend
import com.capstone.trashtotreasure.model.data.local.DetailRecommend.plastic
import com.google.android.play.integrity.internal.c
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.net.URLDecoder

@AndroidEntryPoint
class PlasticDetailRecommendActivity : AppCompatActivity() {

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

        if(result == "Anyaman"){
            if (plastic.isNotEmpty()) {
                craft = plastic[7]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("0WqkqL_D_E0", 0f)
                }
            })
        }else if(result == "Celengan"){
            if (plastic.isNotEmpty()) {
                craft = plastic[6]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("LJq1UUX7Vlo", 0f)
                }
            })
        }else if(result == "Hiasan Dinding"){
            if (plastic.isNotEmpty()) {
                craft = plastic[5]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("c-eM1ItaDlc", 0f)
                }
            })
        }else if(result == "Lampu Hias"){
            if (plastic.isNotEmpty()) {
                craft = plastic[4]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("5eA0z_jVwm4", 0f)
                }
            })
        }else if(result == "Pot Tanaman"){
            if (plastic.isNotEmpty()) {
                craft = plastic[3]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("jBvEWB8Uwn8", 0f)
                }
            })
        }else if(result == "Tempat Sabun"){
            if (plastic.isNotEmpty()) {
                craft = plastic[2]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("OTkwZp_Xyws", 0f)
                }
            })
        }else if(result == "Tempat Penyimpanan"){
            if (plastic.isNotEmpty()) {
                craft = plastic[1]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("NaFKyXxuIfo", 0f)
                }
            })
        }else if(result == "Tempat Pensil"){
            if (plastic.isNotEmpty()) {
                craft = plastic[0]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("o4YxuCxmPjM", 0f)
                }
            })
        }else if(result == "Bunga Plastik"){
            if (plastic.isNotEmpty()) {
                craft = plastic[8]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("kogP14l1PGM", 0f)
                }
            })
        }else if(result == "Gantungan Kunci"){
            if (plastic.isNotEmpty()) {
                craft = plastic[9]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("EROF2tXFzMQ", 0f)
                }
            })
        }else if(result == "Tas (1)"){
            if (plastic.isNotEmpty()) {
                craft = plastic[10]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("9QI0xWc_oQg", 0f)
                }
            })
        }else if(result == "Tempat Tisu"){
            if (plastic.isNotEmpty()) {
                craft = plastic[11]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("x7xIuhKFUgU", 0f)
                }
            })
        }else if(result == "Tikar"){
            if (plastic.isNotEmpty()) {
                craft = plastic[12]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("HAiyzMJzD8o", 0f)
                }
            })
        }else if(result == "Boneka"){
            if (plastic.isNotEmpty()) {
                craft = plastic[13]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("j_r2S6NyLWA", 0f)
                }
            })
        }else if(result == "Keranjang"){
            if (plastic.isNotEmpty()) {
                craft = plastic[14]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("yeHi-50mUlQ", 0f)
                }
            })
        }else if(result == "Keranjang"){
            if (plastic.isNotEmpty()) {
                craft = plastic[14]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("yeHi-50mUlQ", 0f)
                }
            })
        }else if(result == "Pigura Cermin"){
            if (plastic.isNotEmpty()) {
                craft = plastic[15]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("PPcEBk2efew", 0f)
                }
            })
        }else if(result == "Tas"){
            if (plastic.isNotEmpty()) {
                craft = plastic[16]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("X6cksmuwMw4", 0f)
                }
            })
        }else if(result == "Tempat Lilin"){
            if (plastic.isNotEmpty()) {
                craft = plastic[17]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("CTXXzf1UvYc", 0f)
                }
            })
        }else if(result == "Vas Bunga"){
            if (plastic.isNotEmpty()) {
                craft = plastic[18]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("CTXXzf1UvYc", 0f)
                }
            })
        } else if(result == "Hiasan Bunga"){
            if (plastic.isNotEmpty()) {
                craft = plastic[19]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("B1rHCbX62QI", 0f)
                }
            })
        } else if(result == "Hiasan Gantung"){
            if (plastic.isNotEmpty()) {
                craft = plastic[20]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("DQetoWNMwNE", 0f)
                }
            })
        }else if(result == "Kalung"){
            if (plastic.isNotEmpty()) {
                craft = plastic[21]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("J0mdO7TyKqs", 0f)
                }
            })
        }else if(result == "Kerangka Lampu"){
            if (plastic.isNotEmpty()) {
                craft = plastic[22]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("Hbp5L0106qo", 0f)
                }
            })
        }else if(result == "Pigura"){
            if (plastic.isNotEmpty()) {
                craft = plastic[23]
            }
            binding.tvDesc.text = craft.description
            lifecycle.addObserver(binding.ytPlayer)

            binding.ytPlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener(){
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo("HUvnkHG-LqU", 0f)
                }
            })
        }
    }


    companion object{
        const val EXTRA_TITLE = "EXTRA_TITLE"
    }
}