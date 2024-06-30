package com.capstone.trashtotreasure.view.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.capstone.trashtotreasure.utils.Result
import com.capstone.trashtotreasure.databinding.FragmentHomeBinding
import com.capstone.trashtotreasure.model.data.remote.response.getuser.GetUserResponse
import com.capstone.trashtotreasure.utils.NetworkUtil
import com.capstone.trashtotreasure.view.ui.adapter.ArticleAdapter
import com.capstone.trashtotreasure.view.ui.adapter.LoadingStateAdapter
import com.capstone.trashtotreasure.view.ui.login.LoginActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.storage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
@ExperimentalPagingApi
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
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

        checkNetworkAndSession()
        setupViews()
        setupNewsAdapter()
        getArticle()

        binding.loadingShimmerProfile.visibility = View.VISIBLE

        homeViewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) { token ->
            if (token != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    val userLiveData = withContext(Dispatchers.IO) { homeViewModel.getUser(token) }
                    userLiveData.observe(viewLifecycleOwner) { profile ->
                        if (profile != null && viewLifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                            binding.tvName.visibility = View.VISIBLE
                            binding.ivProfile.visibility = View.VISIBLE
                            binding.tvName.text = "Halo " + profile.payload?.nama
                            Glide.with(requireContext())
                                .load(profile?.payload?.photoUrl)
                                .circleCrop()
                                .into(binding.ivProfile)
                            binding.loadingShimmerProfile.visibility = View.INVISIBLE
                        }
                    }
                }
            }
        }

        checkNetworkAndSession()
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
        // Optionally, you can show a retry button or prompt the user to check their connection
    }

    private fun checkSession() {
        homeViewModel.checkIfTokenAvailable().observe(viewLifecycleOwner) {
            if (it == "null") {
                redirectToLogin()
            }
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        checkSession()
        binding.loadingShimmer.visibility = View.VISIBLE
        if (::recyclerView.isInitialized && lastVisiblePosition != RecyclerView.NO_POSITION) {
            recyclerView.scrollToPosition(lastVisiblePosition)
            binding.loadingShimmer.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        if (::recyclerView.isInitialized) {
            lastVisiblePosition = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            binding.loadingShimmer.visibility = View.INVISIBLE
        } else {
            binding.loadingShimmer.visibility = View.VISIBLE
        }
    }

    private fun setupViews() {
        recyclerView = binding.rvArtikel
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupNewsAdapter() {
        newsAdapter = ArticleAdapter()
    }

    private fun getArticle() {
        homeViewModel.getAllArticle().observe(viewLifecycleOwner) { articleData ->
            binding.rvArtikel.adapter = newsAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter { newsAdapter.retry() }
            )
            newsAdapter.submitData(lifecycle, articleData)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



