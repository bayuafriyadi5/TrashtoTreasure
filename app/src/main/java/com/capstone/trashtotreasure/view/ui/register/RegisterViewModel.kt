package com.capstone.trashtotreasure.view.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.capstone.trashtotreasure.model.data.remote.response.user.UserResponse
import com.capstone.trashtotreasure.model.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.io.File
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {
    suspend fun registerUser(
        nama: String, email: String, password: String, telepon: String, alamat: String, photoUrl: File,
    ): LiveData<Result<UserResponse>> = userRepository.register(nama, email, password, telepon, alamat, photoUrl)

}