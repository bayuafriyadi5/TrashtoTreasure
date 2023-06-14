package com.capstone.trashtotreasure.view.ui.plastic.result

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.FragmentHomeBinding
import com.capstone.trashtotreasure.databinding.FragmentPlasticDescBinding
import com.capstone.trashtotreasure.utils.AppExecutors
import com.capstone.trashtotreasure.utils.Result
import com.capstone.trashtotreasure.utils.reduceFileImage
import com.capstone.trashtotreasure.utils.rotateBitmap
import com.capstone.trashtotreasure.view.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PlasticDescFragment : Fragment() {

    private var _binding: FragmentPlasticDescBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlasticResultViewModel by viewModels()

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
        _binding = FragmentPlasticDescBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindResult()
    }

    private fun bindResult() {
        val activity = requireActivity() as PlasticResultActivity
        binding.loading.visibility = VISIBLE
        binding.tvTitle.visibility = GONE
        binding.tvSubtitle.visibility = GONE
        binding.tvCategoryTitle.visibility = GONE
        binding.tvCategorySubtitle.visibility = GONE
        binding.tvDesc.visibility = GONE
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
        viewModel.snapPlastic(file).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.loading.visibility = VISIBLE
                    binding.tvTitle.visibility = GONE
                    binding.tvSubtitle.visibility = GONE
                    binding.tvCategoryTitle.visibility = GONE
                    binding.tvCategorySubtitle.visibility = GONE
                    binding.tvDesc.visibility = GONE
                }
                is Result.Error -> {
                    binding.loading.visibility = GONE

                    binding.tvTitle.visibility = GONE
                    binding.tvSubtitle.visibility = GONE
                    binding.tvCategoryTitle.visibility = GONE
                    binding.tvCategorySubtitle.visibility = GONE
                    binding.tvDesc.visibility = GONE
                    val error = result.error
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    binding.loading.visibility = GONE

                    binding.tvTitle.visibility = VISIBLE
                    binding.tvSubtitle.visibility = VISIBLE
                    binding.tvCategoryTitle.visibility = VISIBLE
                    binding.tvCategorySubtitle.visibility = VISIBLE
                    binding.tvDesc.visibility = VISIBLE

                    binding.tvTitle.text = result.data.answer
                    binding.tvDesc.text = Html.fromHtml(result.data.description)

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