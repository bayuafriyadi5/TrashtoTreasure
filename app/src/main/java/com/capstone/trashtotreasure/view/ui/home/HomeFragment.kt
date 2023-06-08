package com.capstone.trashtotreasure.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.FragmentHomeBinding
import com.capstone.trashtotreasure.view.ui.adapter.ArticleAdapter
import com.capstone.trashtotreasure.view.ui.adapter.LoadingStateAdapter
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private lateinit var newsAdapter: ArticleAdapter
    private lateinit var recyclerView: RecyclerView
    private var lastVisiblePosition: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupUser()
        setupNewsAdapter()
        getArticle()
    }

    override fun onResume() {
        super.onResume()
        binding.loadingShimmer.visibility = View.VISIBLE
        if (::recyclerView.isInitialized && lastVisiblePosition != RecyclerView.NO_POSITION) {
            recyclerView.scrollToPosition(lastVisiblePosition)
            binding.loadingShimmer.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        if (::recyclerView.isInitialized) {
            lastVisiblePosition =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            binding.loadingShimmer.visibility = View.INVISIBLE
        }else{
            binding.loadingShimmer.visibility = View.VISIBLE
        }
    }

    private fun setupViews() {
        recyclerView = binding.rvArtikel
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupUser() {
        auth = Firebase.auth
        val currentUser = auth.currentUser

        binding.tvName.text = "Hi! ${currentUser?.displayName}"
        Glide.with(requireContext())
            .load(currentUser?.photoUrl)
            .circleCrop()
            .into(binding.ivProfile)

        if (currentUser == null) {
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setupNewsAdapter() {
        newsAdapter = ArticleAdapter()
    }

    private fun getArticle() {
        homeViewModel.getAllArticle().observe(viewLifecycleOwner) { articleData ->
            binding.rvArtikel.adapter = newsAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    newsAdapter.retry()
                }
            )
            newsAdapter.submitData(lifecycle, articleData)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
