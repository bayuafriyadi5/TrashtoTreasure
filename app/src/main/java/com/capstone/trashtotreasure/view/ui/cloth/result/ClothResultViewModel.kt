package com.capstone.trashtotreasure.view.ui.cloth.result

import androidx.lifecycle.ViewModel
import com.capstone.trashtotreasure.model.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject


@HiltViewModel
class ClothResultViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    fun snapCloth(file: File) = photoRepository.postSnapKain(file)
}