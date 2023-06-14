package com.capstone.trashtotreasure.view.ui.metal

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityMetalSnapBinding
import com.capstone.trashtotreasure.databinding.ActivityPlasticSnapBinding
import com.capstone.trashtotreasure.utils.fromUriToFile
import com.capstone.trashtotreasure.view.ui.metal.result.MetalResultActivity
import com.capstone.trashtotreasure.view.ui.plastic.PlasticCameraActivity
import com.capstone.trashtotreasure.view.ui.plastic.result.PlasticResultActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MetalSnapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMetalSnapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMetalSnapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        binding.btnCamera.setOnClickListener {
            Toast.makeText(this, getString(R.string.opening_camera), Toast.LENGTH_SHORT).show()
            if (!allPermissionsGranted()) {
                ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                )
            } else {
                val intent = Intent(this, MetalCameraActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnGallery.setOnClickListener {
            Toast.makeText(this, getString(R.string.opening_gallery), Toast.LENGTH_SHORT).show()
            startGallery()
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.import_picture))
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val selectedImage: Uri = it.data?.data as Uri
            val myFile = fromUriToFile(selectedImage, this)
            val intent = Intent(this, MetalResultActivity::class.java)
            intent.putExtra(MetalResultActivity.EXTRA_PICTURE, myFile)
            intent.putExtra(MetalResultActivity.EXTRA_IS_FROM_GALLERY, true)
            startActivity(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    getString(R.string.permission_not_granted),
                    Toast.LENGTH_SHORT
                ).show()
                this.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            this.baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}