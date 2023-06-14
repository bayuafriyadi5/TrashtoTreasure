package com.capstone.trashtotreasure.view.ui.plastic.result

import androidx.lifecycle.ViewModel
import com.capstone.trashtotreasure.model.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PlasticResultViewModel @Inject constructor(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    fun snapPlastic(file: File) = photoRepository.postSnapPlastic(file)
}