package com.capstone.trashtotreasure.view.ui.plastic.result

import android.Manifest
import android.content.Intent
import android.graphics.BitmapFactory
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import com.capstone.trashtotreasure.utils.Result
import androidx.recyclerview.widget.LinearLayoutManager
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityPlasticResultBinding
import com.capstone.trashtotreasure.utils.AppExecutors
import com.capstone.trashtotreasure.utils.reduceFileImage
import com.capstone.trashtotreasure.utils.rotateBitmap
import com.capstone.trashtotreasure.view.ui.adapter.PlasticPagerAdapter
import com.capstone.trashtotreasure.view.ui.plastic.PlasticSnapActivity
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class PlasticResultActivity : AppCompatActivity() {

    private val binding: ActivityPlasticResultBinding by lazy {
        ActivityPlasticResultBinding.inflate(layoutInflater)
    }

    private val viewModel: PlasticResultViewModel by viewModels()

    private val appExecutor: AppExecutors by lazy {
        AppExecutors()
    }

    private var file: File? = null
    private var isBack: Boolean = true
    private var compressingDone: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val sectionsPagerAdapter = PlasticPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        bindResult()

        binding.btnBack.setOnClickListener {
           onBackPressed()
        }
    }

    private fun bindResult() {
        binding.ivPreview.visibility = GONE
        binding.tvTitle.visibility = GONE
        binding.tabs.visibility = GONE
        binding.errorAnimation.visibility = GONE

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
        viewModel.snapPlastic(file).observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.tabs.visibility = GONE
                    binding.errorAnimation.visibility = GONE
                }
                is Result.Error -> {
                    binding.ivPreview.visibility = GONE
                    binding.tvTitle.visibility = GONE
                    binding.tabs.visibility = GONE
                    binding.errorAnimation.visibility = VISIBLE
                    val error = result.error
                    Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    binding.tabs.visibility = VISIBLE
                    binding.ivPreview.visibility = VISIBLE
                    binding.tvTitle.visibility = VISIBLE
                    binding.errorAnimation.visibility = GONE
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