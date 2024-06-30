package com.capstone.trashtotreasure.view.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.trashtotreasure.model.data.local.preferences.AuthPreferences
import com.capstone.trashtotreasure.model.data.remote.response.login.LoginResponse
import com.capstone.trashtotreasure.model.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authPreferences: AuthPreferences
) : ViewModel() {
    suspend fun login(email: String, password: String): LiveData<Result<LoginResponse>> =
        userRepository.login(email, password)

    fun saveToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("LoginViewModel", "Saving token: $token")
            authPreferences.saveToken(token)
            Log.d("LoginViewModel", "Token saved")
        }
    }

}
