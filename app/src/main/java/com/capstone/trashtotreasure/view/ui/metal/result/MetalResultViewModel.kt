package com.capstone.trashtotreasure.view.ui.metal.result

import androidx.lifecycle.ViewModel
import com.capstone.trashtotreasure.model.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MetalResultViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    fun snapMetal(file: File) = photoRepository.postSnapMetal(file)
}