package com.capstone.trashtotreasure.view.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.databinding.FragmentProfileBinding
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val profileViewModel: ProfileViewModel by viewModels()
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

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

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        binding.tvProfileName.text = firebaseUser?.displayName.toString()
        binding.tvEmail.text = firebaseUser?.email.toString()
        Glide.with(requireContext())
            .load(firebaseUser?.photoUrl)
            .circleCrop()
            .into(binding.ivProfile)


        if (firebaseUser == null) {
            // Not signed in, launch the Login activity
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
            return
        }

        binding.btnSignOut.setOnClickListener {
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Sign Out")
                setMessage("Are you sure want to log out?")
                setPositiveButton("Yes") { _, _ ->
                    signOut()
                }
                setNegativeButton("no"){ dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }


    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}