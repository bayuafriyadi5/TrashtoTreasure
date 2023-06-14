package com.capstone.trashtotreasure.view.ui.metal.result

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.FragmentMetalRecommendBinding
import com.capstone.trashtotreasure.databinding.FragmentPlasticRecommendBinding
import com.capstone.trashtotreasure.utils.AppExecutors
import com.capstone.trashtotreasure.utils.Result
import com.capstone.trashtotreasure.utils.reduceFileImage
import com.capstone.trashtotreasure.utils.rotateBitmap
import com.capstone.trashtotreasure.view.ui.adapter.MetalRecommendAdapter
import com.capstone.trashtotreasure.view.ui.adapter.MetalRecommendationItem
import com.capstone.trashtotreasure.view.ui.plastic.result.PlasticResultActivity
import com.capstone.trashtotreasure.view.ui.plastic.result.PlasticResultViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MetalRecommendFragment : Fragment() {

    private var _binding: FragmentMetalRecommendBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MetalResultViewModel by viewModels()

    private val appExecutor: AppExecutors by lazy {
        AppExecutors()
    }

    private var file: File? = null
    private var isBack: Boolean = true
    private var compressingDone: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMetalRecommendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindResult()
    }

    private fun bindResult() {
        binding.loadingShimmer.visibility = View.VISIBLE
        val activity = requireActivity() as MetalResultActivity
        file = activity.intent.getSerializableExtra(EXTRA_PICTURE) as File
        isBack = activity.intent.getBooleanExtra(EXTRA_IS_BACK_CAMERA, true)
        val isGallery = activity.intent.getBooleanExtra(EXTRA_IS_FROM_GALLERY, false)


        var result = BitmapFactory.decodeFile((file as File).path)
        if (!isGallery) {
            result = rotateBitmap(result, isBack)
        }

        appExecutor.diskIO.execute {
            file = reduceFileImage(file as File)
            compressingDone.postValue(true)
        }

        compressingDone.observe(viewLifecycleOwner) { done ->
            if (done) {
                file?.let { loadResult(it) }
            }
        }

    }

    private fun loadResult(file: File) {
        getResults(file)
    }

    private fun getResults(file: File) {
        viewModel.snapMetal(file).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.loadingShimmer.visibility = View.VISIBLE
                }
                is Result.Error -> {
                    binding.loadingShimmer.visibility = View.INVISIBLE
                    val error = result.error
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    binding.loadingShimmer.visibility = View.INVISIBLE
                    val data = result.data.fileUrls
                    val recommendationItems = data?.map { MetalRecommendationItem(it.orEmpty()) } ?: emptyList()
                    binding.rvRecommend.apply {
                        adapter = MetalRecommendAdapter(recommendationItems)
                        layoutManager = GridLayoutManager(requireActivity(), 2)
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_PICTURE = "extra_picture"
        const val EXTRA_IS_FROM_GALLERY = "extra_is_from_gallery"
        const val EXTRA_IS_BACK_CAMERA = "extra_is_back_camera"

    }

}