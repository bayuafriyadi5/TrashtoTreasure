package com.capstone.trashtotreasure.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityMainBinding
import com.capstone.trashtotreasure.utils.NetworkUtil
import com.capstone.trashtotreasure.view.ui.home.HomeViewModel
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.capstone.trashtotreasure.view.ui.profile.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        checkNetworkAndSession()

        profileViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_camera, R.id.navigation_market, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Observe the current destination to hide/show toolbar
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_market) {
                binding.toolbarMain.visibility = View.GONE
            } else {
                binding.toolbarMain.visibility = View.VISIBLE
            }
        }
    }

    private fun checkNetworkAndSession() {
            if (!NetworkUtil.isNetworkAvailable(this)) {
                showNetworkError()
            } else {
                checkSession()
            }
    }

    private fun showNetworkError() {
        Toast.makeText(this, "No internet connection...", Toast.LENGTH_LONG).show()
        // Optionally, you can direct the user to a no internet activity or show a retry button
    }

    override fun onResume() {
        super.onResume()
        checkSession()
    }

    private fun checkSession() {
        homeViewModel.checkIfTokenAvailable().observe(this) {
            if (it == null) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}
