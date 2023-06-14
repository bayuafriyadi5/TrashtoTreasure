package com.capstone.trashtotreasure.view.ui.feedback

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityFeedbackBinding
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import java.util.*
@AndroidEntryPoint
class FeedbackActivity : AppCompatActivity() {

    private val binding: ActivityFeedbackBinding by lazy {
        ActivityFeedbackBinding.inflate(layoutInflater)
    }

    private val viewModel: FeedbackViewModel by viewModels ()

    private lateinit var auth: FirebaseAuth

    private lateinit var ratings: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.loading.visibility = View.INVISIBLE

        setupAction()


    }

    private fun setupAction() {

        binding.rbFeedback.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            val selectedRating = ratingBar.rating
            ratings = selectedRating.toString()
        }

        binding.btnFeedback.setOnClickListener {
            val rate = "Bintang $ratings"
            val text = binding.textEditText.text.toString()


            auth = Firebase.auth
            val firebaseUser = auth.currentUser
            val email = firebaseUser?.email.toString()

            binding.loading.visibility = View.VISIBLE


            when {

                text.isEmpty() -> {
                    binding.textEditTextLayout.error = "Please insert your feedback"
                    binding.loading.visibility = View.INVISIBLE
                }
                else -> {
                    lifecycleScope.launchWhenResumed {
                        launch {
                            viewModel.postFeedback(email, rate, text)
                                .observe(this@FeedbackActivity) { result ->
                                    result.onSuccess {
                                        binding.loading.visibility = View.INVISIBLE
                                        AlertDialog.Builder(this@FeedbackActivity).apply {
                                            setTitle("Yeah!")
                                            setMessage(R.string.alert_feedback)
                                            setPositiveButton("Close") { _, _ ->
                                                finish()
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                    result.onFailure {
                                        val error = it.message
                                        Toast.makeText(
                                            this@FeedbackActivity,
                                            error,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.loading.visibility = View.INVISIBLE
                                    }

                                }
                        }
                    }
                }
            }
        }
    }
}