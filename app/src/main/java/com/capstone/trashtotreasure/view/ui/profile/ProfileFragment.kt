package com.capstone.trashtotreasure.view.ui.profile

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.FragmentProfileBinding
import com.capstone.trashtotreasure.utils.NetworkUtil
import com.capstone.trashtotreasure.view.ui.cloth.ClothSnapActivity
import com.capstone.trashtotreasure.view.ui.feedback.FeedbackActivity
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
@ExperimentalPagingApi
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val profileViewModel: ProfileViewModel by viewModels()
    private val binding get() = _binding!!
    private var email: String? = null
    private var token: String = "null"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkNetworkAndSession()

        profileViewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) { token ->
            this.token = token ?: "null"

            if (token != "null") {
                viewLifecycleOwner.lifecycleScope.launch {
                    val userLiveData =
                        withContext(Dispatchers.IO) { profileViewModel.getUser(token!!) }
                    userLiveData.observe(viewLifecycleOwner) { profile ->
                        if (profile != null && viewLifecycleOwner.lifecycle.currentState.isAtLeast(
                                Lifecycle.State.STARTED
                            )
                        ) {
                            binding.tvProfileName.visibility = View.VISIBLE
                            binding.tvEmail.visibility = View.VISIBLE
                            binding.ivProfile.visibility = View.VISIBLE
                            binding.bannerProfile.visibility = View.VISIBLE

                            binding.tvProfileName.text = profile.payload?.nama
                            binding.tvEmail.text = profile.payload?.email
                            Glide.with(requireContext())
                                .load(profile?.payload?.photoUrl)
                                .circleCrop()
                                .into(binding.ivProfile)
                            email = profile.payload?.email // Save email to variable
                            // Use email in other methods or for further processing
                            loadPenjualData(email) // Example usage
                            binding.loadingShimmerProfile.visibility = View.INVISIBLE
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Profile not available",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.btnSignOut.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Sign Out")
                setMessage(R.string.alert_logout)
                setPositiveButton("Yes") { _, _ ->
                    profileViewModel.logout()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                setNegativeButton("no") { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }


        profileViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            profileViewModel.saveThemeSetting(isChecked)
        }

        binding.menuLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.menuFeedback.setOnClickListener {
            val intent = Intent(requireContext(), FeedbackActivity::class.java)
            startActivity(intent)
        }

        binding.menuEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.menuPesananMasuk.setOnClickListener {
            val intent = Intent(requireContext(), PesananMasukActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkNetworkAndSession() {
        if (!NetworkUtil.isNetworkAvailable(requireContext())) {
            showNetworkError()
        } else {
            checkSession()
        }
    }

    private fun showNetworkError() {
        Toast.makeText(requireContext(), "No internet connection...", Toast.LENGTH_LONG).show()
        // Optionally, you can direct the user to a no internet activity or show a retry button
    }

    override fun onResume() {
        super.onResume()
        checkSession()
    }

    private fun loadPenjualData(email: String?) {
        email?.let {
            viewLifecycleOwner.lifecycleScope.launch {
                val userLiveData = withContext(Dispatchers.IO) { profileViewModel.getPenjualByEmail(token, it) }
                userLiveData.observe(viewLifecycleOwner) { profile ->
                    if (profile != null && viewLifecycleOwner.lifecycle.currentState.isAtLeast(
                            Lifecycle.State.STARTED)) {
                        val emailPenjual =profile.payload?.email
                        if (emailPenjual != null){
                            binding.tvRegisPenjual.setText(R.string.update_seller)
                            binding.menuDaftarPenjual.setOnClickListener {
                                val intent = Intent(requireContext(), UpdateSellerActivity::class.java)
                                startActivity(intent)
                            }
                            binding.menuPesananMasuk.visibility = View.VISIBLE
                        }else{
                            binding.menuPesananMasuk.visibility = View.GONE
                            binding.menuDaftarPenjual.setOnClickListener {
                                val intent = Intent(requireContext(), RegisSellerActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    } else {
                        startActivity(Intent(requireContext(), LoginActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }
        }
    }

    private fun checkSession() {
        profileViewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) {
            if (it == "null") {
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
