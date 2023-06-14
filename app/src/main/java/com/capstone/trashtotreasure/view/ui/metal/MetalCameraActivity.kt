package com.capstone.trashtotreasure.view.ui.metal

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import com.capstone.trashtotreasure.R
import com.capstone.trashtotreasure.databinding.ActivityMetalCameraBinding
import com.capstone.trashtotreasure.databinding.ActivityPlasticCameraBinding
import com.capstone.trashtotreasure.utils.createFile
import com.capstone.trashtotreasure.view.ui.metal.result.MetalResultActivity
import com.capstone.trashtotreasure.view.ui.plastic.result.PlasticResultActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
@AndroidEntryPoint
class MetalCameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMetalCameraBinding
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    private var imageCapture: ImageCapture? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMetalCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraExecutor = Executors.newSingleThreadExecutor()
        setupView()
    }

    private fun setupView() {
        binding.captureImage.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            Toast.makeText(this, "Capturing photo, please wait", Toast.LENGTH_SHORT).show()
            takePhoto()
        }
    }

    public override fun onResume() {
        super.onResume()
        hideSystemUI()
        startCamera()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun hideSystemUI() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun startCamera() {
        val cameraProvideFuture = ProcessCameraProvider.getInstance(this)

        cameraProvideFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProvideFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                Toast.makeText(this, getString(R.string.failed_open_camera), Toast.LENGTH_SHORT)
                    .show()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    proceedPicture(photoFile)
                }

                override fun onError(exception: ImageCaptureException) {

                }
            }
        )
    }

    private fun proceedPicture(photoFile: File) {
        val intent = Intent(this, MetalResultActivity::class.java)
        intent.putExtra(MetalResultActivity.EXTRA_PICTURE, photoFile)
        intent.putExtra(
            MetalResultActivity.EXTRA_IS_BACK_CAMERA,
            cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
        )
        startActivity(intent)
        finish()
    }
}