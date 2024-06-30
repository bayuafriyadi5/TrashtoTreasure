package com.capstone.trashtotreasure.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.trashtotreasure.model.data.local.preferences.AuthPreferences
import com.capstone.trashtotreasure.model.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authPreferences: AuthPreferences,
    private val userRepository: UserRepository
) : ViewModel() {

    fun setFirstTime(firstTime: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            authPreferences.setFirstTime(firstTime)
        }
    }

    fun checkIfTokenAvailable(): LiveData<String?> = userRepository.getToken()

}