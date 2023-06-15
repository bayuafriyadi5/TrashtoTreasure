package com.capstone.trashtotreasure.view.ui.cloth.result

import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.lifecycle.MutableLiveData
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityClothResultBinding
import com.capstone.trashtotreasure.databinding.ActivityGlassResultBinding
import com.capstone.trashtotreasure.utils.AppExecutors
import com.capstone.trashtotreasure.utils.Result
import com.capstone.trashtotreasure.utils.reduceFileImage
import com.capstone.trashtotreasure.utils.rotateBitmap
import com.capstone.trashtotreasure.view.ui.adapter.ClothPagerAdapter
import com.capstone.trashtotreasure.view.ui.adapter.GlassPagerAdapter
import com.capstone.trashtotreasure.view.ui.glass.result.GlassResultActivity
import com.capstone.trashtotreasure.view.ui.glass.result.GlassResultViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class ClothResultActivity : AppCompatActivity() {

    private val binding: ActivityClothResultBinding by lazy {
        ActivityClothResultBinding.inflate(layoutInflater)
    }

    private val viewModel: ClothResultViewModel by viewModels()

    private val appExecutor: AppExecutors by lazy {
        AppExecutors()
    }

    private var file: File? = null
    private var isBack: Boolean = true
    private var compressingDone: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sectionsPagerAdapter = ClothPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(ClothResultActivity.TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        bindResult()

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun bindResult() {
        binding.ivPreview.visibility = View.GONE
        binding.tvTitle.visibility = View.GONE
        binding.tabs.visibility = View.GONE
        binding.errorAnimation.visibility = View.GONE

        file = intent.getSerializableExtra(EXTRA_PICTURE) as File
        isBack = intent.getBooleanExtra(EXTRA_IS_BACK_CAMERA, true)
        val isGallery = intent.getBooleanExtra(EXTRA_IS_FROM_GALLERY, false)



        var result = BitmapFactory.decodeFile((file as File).path)
        if (!isGallery) {
            result = rotateBitmap(result, isBack)
        }

        appExecutor.diskIO.execute {
            file = reduceFileImage(file as File)
            compressingDone.postValue(true)
        }

        compressingDone.observe(this) { done ->
            if (done) {
                file?.let { loadResult(it) }
                binding.ivPreview.setImageBitmap(result)
            }
        }

    }

    private fun loadResult(file: File) {
        getResults(file)
    }

    private fun getResults(file: File) {
        viewModel.snapCloth(file).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.tabs.visibility = View.GONE
                    binding.errorAnimation.visibility = View.GONE
                }
                is Result.Error -> {
                    binding.ivPreview.visibility = View.GONE
                    binding.tvTitle.visibility = View.GONE
                    binding.tabs.visibility = View.GONE
                    binding.errorAnimation.visibility = View.VISIBLE
                    val error = result.error
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    binding.tabs.visibility = View.VISIBLE
                    binding.ivPreview.visibility = View.VISIBLE
                    binding.tvTitle.visibility = View.VISIBLE
                    binding.errorAnimation.visibility = View.GONE
                    binding.tvTitle.text = result.data.answer
                }
            }
        }
    }



    companion object {
        const val EXTRA_PICTURE = "extra_picture"
        const val EXTRA_IS_FROM_GALLERY = "extra_is_from_gallery"
        const val EXTRA_IS_BACK_CAMERA = "extra_is_back_camera"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_plastic_1,
            R.string.tab_plastic_2
        )
    }
}