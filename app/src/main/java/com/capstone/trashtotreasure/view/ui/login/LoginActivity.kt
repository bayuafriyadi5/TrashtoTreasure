package com.capstone.trashtotreasure.view.ui.login

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityLoginBinding
import com.capstone.trashtotreasure.view.MainActivity
import com.capstone.trashtotreasure.view.ui.register.RegisterActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
@ExperimentalPagingApi
class LoginActivity : AppCompatActivity() {

    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.loading.visibility = View.INVISIBLE
        binding.signInButton.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            signIn()
        }
    }

    private fun signIn() {
        val email = binding.emailEditTextLogin.text.toString()
        val password = binding.passwordEditTextLogin.text.toString()
        val EMAILFORMAT = Regex("[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+")

        when {
            email.isEmpty() -> {
                binding.textInputEmailLogin.error = "Please insert your email"
                binding.loading.visibility = View.INVISIBLE
            }
            password.isEmpty() -> {
                binding.textInputPasswordLogin.error = "Please insert your password"
                binding.loading.visibility = View.INVISIBLE
            }
            password.length < 8 -> {
                Toast.makeText(this, "Password must be 8 or more characters", Toast.LENGTH_SHORT).show()
                binding.loading.visibility = View.INVISIBLE
            }
            !email.matches(EMAILFORMAT) -> {
                binding.textInputEmailLogin.error = "Incorrect email format"
                binding.loading.visibility = View.INVISIBLE
            }
            else -> {
                lifecycleScope.launch {
                    loginViewModel.login(email, password).observe(this@LoginActivity) { result ->
                        result.onSuccess { response ->
                            response.payload?.token?.let { token ->
                                loginViewModel.saveToken(token)
                                binding.loading.visibility = View.INVISIBLE
                                navigateToMainActivity()
                                Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
                            }
                        }
                        result.onFailure {
                            binding.loading.visibility = View.INVISIBLE
                            val error = it.message
                            Toast.makeText(this@LoginActivity, error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun navigateToMainActivity() {

        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            finish()
        }
    }


    fun gotoRegis(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


}
