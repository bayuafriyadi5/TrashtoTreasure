package com.capstone.trashtotreasure.view.ui.register

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityLoginBinding
import com.capstone.trashtotreasure.databinding.ActivityRegisterBinding
import com.capstone.trashtotreasure.utils.fromUriToFile
import com.capstone.trashtotreasure.utils.reduceFileImage
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File

@AndroidEntryPoint
@ExperimentalPagingApi
class RegisterActivity : AppCompatActivity() {

    private val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private var getFile: File? = null
    private val registerViewModel: RegisterViewModel by viewModels ()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.addPhoto.setOnClickListener { startGallery() }
        binding.loading.visibility = View.INVISIBLE
        binding.registerButton.setOnClickListener {
            val nama = binding.namaEditTextRegis.text.toString()
            val email = binding.emailEditTextRegis.text.toString()
            val password = binding.passwordEditTextRegis.text.toString()
            val telepon = binding.telpEditTextRegis.text.toString()
            val alamat = binding.alamatEditTextRegis.text.toString()
            val EMAILFORMAT = Regex("[a-zA-Z0-9._]+@[a-z]+\\.+[a-z]+")
            binding.loading.visibility = View.VISIBLE

            when {
                nama.isEmpty() -> {
                    binding.textInputName.error = "Please insert your name"
                    binding.loading.visibility = View.INVISIBLE
                }
                email.isEmpty() -> {
                    binding.textInputEmail.error = "Please insert your email"
                    binding.loading.visibility = View.INVISIBLE
                }
                !email.matches(EMAILFORMAT) -> {
                    binding.textInputEmail.error = "Incorrect email format"
                    binding.loading.visibility = View.INVISIBLE
                }
                password.length < 8 -> {
                    Toast.makeText(
                        this,
                        "Password must be 8 or more characters",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.loading.visibility = View.INVISIBLE
                }
                password.isEmpty() -> {
                    binding.textInputPassword.error = "Please insert your password"
                    binding.loading.visibility = View.INVISIBLE
                }
                telepon.isEmpty() -> {
                    binding.textInputPassword.error = "Please insert your phone"
                    binding.loading.visibility = View.INVISIBLE
                }
                else -> {
                    lifecycleScope.launchWhenResumed {
                        launch {
                            if (getFile != null) {
                                val file = reduceFileImage(getFile as File)
                                registerViewModel.registerUser(nama, email, password, telepon, alamat, file)
                                    .observe(this@RegisterActivity) { result ->
                                        result.onSuccess {
                                            binding.loading.visibility = View.INVISIBLE
                                            AlertDialog.Builder(this@RegisterActivity).apply {
                                                setTitle("Yeah!")
                                                setMessage("Your account has been successfully created ")
                                                setPositiveButton("Next") { _, _ ->
                                                    finish()
                                                }
                                                create()
                                                show()
                                            }
                                        }
                                        result.onFailure {
                                            val error = it.message
                                            Toast.makeText(
                                                this@RegisterActivity,
                                                error,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            binding.loading.visibility = View.INVISIBLE
                                        }

                                    }
                            }else{
                                showToast(this@RegisterActivity, getString(R.string.select_image))
                                binding.loading.visibility = View.INVISIBLE
                            }

                        }
                    }
                }
            }
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri


            selectedImg.let { uri ->
                val myFile = fromUriToFile(uri, this)
                getFile = myFile
                Glide.with(this)
                    .load(uri)
                    .circleCrop()
                    .into(binding.ivProfile)
            }
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }
    fun gotoLogin(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}