package com.capstone.trashtotreasure.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivitySplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 3000 //3 seconds
    private lateinit var auth: FirebaseAuth

    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        val view = window.decorView
        view.postDelayed({
            val intent = Intent(this, GuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }, SPLASH_TIME_OUT.toLong())

        playAnimation()

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(1000)
        AnimatorSet().apply {
            playSequentially(logo)
            start()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null){
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}