package com.capstone.trashtotreasure.view.ui.cloth.result

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.FragmentClothDescBinding
import com.capstone.trashtotreasure.utils.AppExecutors
import com.capstone.trashtotreasure.utils.Result
import com.capstone.trashtotreasure.utils.reduceFileImage
import com.capstone.trashtotreasure.utils.rotateBitmap
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ClothDescFragment : Fragment() {

    private var _binding: FragmentClothDescBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ClothResultViewModel by viewModels()

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
        _binding = FragmentClothDescBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        bindResult()
    }

    private fun bindResult() {
        val activity = requireActivity() as ClothResultActivity
        binding.loading.visibility = View.VISIBLE
        binding.tvTitle.visibility = View.GONE
        binding.tvSubtitle.visibility = View.GONE
        binding.tvCategoryTitle.visibility = View.GONE
        binding.tvCategorySubtitle.visibility = View.GONE
        binding.tvDesc.visibility = View.GONE
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
        viewModel.snapCloth(file).observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.loading.visibility = View.VISIBLE
                    binding.tvTitle.visibility = View.GONE
                    binding.tvSubtitle.visibility = View.GONE
                    binding.tvCategoryTitle.visibility = View.GONE
                    binding.tvCategorySubtitle.visibility = View.GONE
                    binding.tvDesc.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.loading.visibility = View.GONE

                    binding.tvTitle.visibility = View.GONE
                    binding.tvSubtitle.visibility = View.GONE
                    binding.tvCategoryTitle.visibility = View.GONE
                    binding.tvCategorySubtitle.visibility = View.GONE
                    binding.tvDesc.visibility = View.GONE
                    val error = result.error
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    binding.loading.visibility = View.GONE

                    binding.tvTitle.visibility = View.VISIBLE
                    binding.tvSubtitle.visibility = View.VISIBLE
                    binding.tvCategoryTitle.visibility = View.VISIBLE
                    binding.tvCategorySubtitle.visibility = View.VISIBLE
                    binding.tvDesc.visibility = View.VISIBLE

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