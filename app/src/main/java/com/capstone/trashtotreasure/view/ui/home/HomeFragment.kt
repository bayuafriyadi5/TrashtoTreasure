package com.capstone.trashtotreasure.view.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.model.data.Result
import com.capstone.trashtotreasure.databinding.FragmentHomeBinding
import com.capstone.trashtotreasure.model.data.local.entitiy.ArticleEntity
import com.capstone.trashtotreasure.view.ui.adapter.ArticleAdapter
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var newsAdapter: ArticleAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        val firebaseUser = auth.currentUser

        binding.loadingShimmer.visibility = View.VISIBLE
        binding.rvArtikel.visibility = View.GONE

        binding.tvName.text = "Hi! "+firebaseUser?.displayName.toString()
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

        newsAdapter = ArticleAdapter { news ->
            if (news.isBookmarked){
                homeViewModel.deleteNews(news)
            } else {
                homeViewModel.saveNews(news)
            }
        }


        homeViewModel.getAllArticle().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.loadingShimmer.visibility = View.VISIBLE
                        binding.rvArtikel.visibility = View.GONE
                    }
                    is Result.Success -> {
                        val newsData = result.data
                        newsAdapter.setData(newsData)
                        Log.d("ArticleAdapter", "News list size: ${newsData.size}")
                        binding.loadingShimmer.visibility = View.INVISIBLE
                        binding.rvArtikel.visibility = View.VISIBLE
                    }
                    is Result.Error -> {
                        binding.loadingShimmer.visibility = View.INVISIBLE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvArtikel.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = newsAdapter
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}