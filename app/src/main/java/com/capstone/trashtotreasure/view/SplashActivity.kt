package com.capstone.trashtotreasure.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivitySplashBinding
import com.capstone.trashtotreasure.utils.NetworkUtil
import com.capstone.trashtotreasure.view.ui.home.HomeViewModel
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.capstone.trashtotreasure.view.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalPagingApi
class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT = 3000 // 3 seconds
    private lateinit var auth: FirebaseAuth
    private var token: String = "null"
    private val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }
    private val profileViewModel: ProfileViewModel by viewModels()
    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        playAnimation()
        checkNetworkAndSession()
        observeToken()
        observeThemeSettings()
    }

    private fun observeToken() {
        splashViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"
        }
    }

    private fun observeThemeSettings() {
        profileViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun checkNetworkAndSession() {
        val view = window.decorView
        view.postDelayed({
            if (!NetworkUtil.isNetworkAvailable(this)) {
                showNetworkError()
                checkIfSessionValid()
            } else {
                checkIfSessionValid()
            }
        }, SPLASH_TIME_OUT.toLong())
    }

    private fun showNetworkError() {
        Toast.makeText(this, "No internet connection...", Toast.LENGTH_LONG).show()
        // Optionally, you can direct the user to a no internet activity or show a retry button
    }

    private fun checkIfSessionValid() {
        if (token == "null") {
            val intent = Intent(this, GuideActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            splashViewModel.setFirstTime(true)
            finish()
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            splashViewModel.setFirstTime(false)
            finish()
        }
    }

    private fun playAnimation() {
        val logo = ObjectAnimator.ofFloat(binding.logo, View.ALPHA, 1f).setDuration(1000)
        AnimatorSet().apply {
            playSequentially(logo)
            start()
        }
    }
}
