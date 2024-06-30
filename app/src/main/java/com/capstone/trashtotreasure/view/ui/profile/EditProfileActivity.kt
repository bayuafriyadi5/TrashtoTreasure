package com.capstone.trashtotreasure.view.ui.profile

import android.app.ProgressDialog.show
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityEditProfileBinding
import com.capstone.trashtotreasure.databinding.ActivityRegisterBinding
import com.capstone.trashtotreasure.utils.fromUriToFile
import com.capstone.trashtotreasure.utils.reduceFileImage
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


@AndroidEntryPoint
@ExperimentalPagingApi
class EditProfileActivity : AppCompatActivity() {

    private val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    private val profileViewModel: ProfileViewModel by viewModels()

    private var token: String = "null"
    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.loading.visibility = View.INVISIBLE

        profileViewModel.checkIfTokenAvailable().observe(this) { token ->
            this.token = token ?: "null"

            if (token != "null") {
                this.lifecycleScope.launch {
                    val userLiveData = withContext(Dispatchers.IO) { profileViewModel.getUser(token!!) }
                    userLiveData.observe(this@EditProfileActivity) { profile ->
                        if (profile != null && this@EditProfileActivity.lifecycle.currentState.isAtLeast(
                                Lifecycle.State.STARTED)) {
                            binding.namaEditTextRegis.setText(profile.payload?.nama)
                            binding.emailEditTextRegis.setText(profile.payload?.email)
                            binding.telpEditTextRegis.setText(profile.payload?.telepon)
                            binding.alamatEditTextRegis.setText(profile.payload?.alamat)
                            Glide.with(this@EditProfileActivity)
                                .load(profile.payload?.photoUrl)
                                .circleCrop()
                                .into(binding.ivProfile)
                        } else {
                            Toast.makeText(this@EditProfileActivity, "Profile not available", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        binding.addPhoto.setOnClickListener { startGallery() }

        binding.updateButton.setOnClickListener {
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
                                profileViewModel.update(token, nama, email, password, telepon, alamat, file)
                                    .observe(this@EditProfileActivity) { result ->
                                        handleUpdateResult(result)
                                    }
                            } else {
                                profileViewModel.updatenoimage(token, nama, email, password, telepon, alamat)
                                    .observe(this@EditProfileActivity) { result ->
                                        handleUpdateResult(result)
                                    }
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

    private fun handleUpdateResult(result: Result<Any>) {
        result.onSuccess {
            binding.loading.visibility = View.INVISIBLE
            AlertDialog.Builder(this@EditProfileActivity).apply {
                setTitle("Yeah!")
                setMessage("Your account has been successfully updated")
                setPositiveButton("Next") { _, _ -> finish() }
                create()
                show()
            }
        }
        result.onFailure {
            val error = it.message
            Toast.makeText(this@EditProfileActivity, error, Toast.LENGTH_SHORT).show()
            binding.loading.visibility = View.INVISIBLE
        }
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
