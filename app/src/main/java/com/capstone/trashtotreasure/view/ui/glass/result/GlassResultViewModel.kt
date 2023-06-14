package com.capstone.trashtotreasure.view.ui.glass.result

import androidx.lifecycle.ViewModel
import com.capstone.trashtotreasure.model.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class GlassResultViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    fun snapGlass(file: File) = photoRepository.postSnapKaca(file)
}