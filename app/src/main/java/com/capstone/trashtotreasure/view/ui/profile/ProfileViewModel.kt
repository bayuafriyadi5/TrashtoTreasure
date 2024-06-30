package com.capstone.trashtotreasure.view.ui.profile

import androidx.lifecycle.*
import com.capstone.trashtotreasure.model.data.local.preferences.AuthPreferences
import com.capstone.trashtotreasure.model.data.local.preferences.SettingPreferences
import com.capstone.trashtotreasure.model.data.remote.response.getPenjualEmail.GetPenjualEmailResponse
import com.capstone.trashtotreasure.model.data.remote.response.getuser.GetUserResponse
import com.capstone.trashtotreasure.model.data.remote.response.regisSeller.RegisSellerResponse
import com.capstone.trashtotreasure.model.data.remote.response.user.UserResponse
import com.capstone.trashtotreasure.model.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val pref: SettingPreferences,
    private val userRepository: UserRepository,
    private val authPreferences: AuthPreferences,
    ): ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    suspend fun getUser(token: String): LiveData<GetUserResponse?> =
        userRepository.getUser(token)

    suspend fun update(token: String,
        nama: String, email: String, password: String, telepon: String, alamat: String, photoUrl: File,
    ): LiveData<Result<UserResponse>> = userRepository.update(token, nama, email, password, telepon, alamat, photoUrl)


    suspend fun updatenoimage(token: String,
                      nama: String, email: String, password: String, telepon: String, alamat: String,): LiveData<Result<UserResponse>> =
        userRepository.updatenoimage(token, nama, email, password, telepon, alamat)

    suspend fun regisSeller(token: String,
                              noRekening: String,): LiveData<Result<RegisSellerResponse>> =
        userRepository.regisSeller(token, noRekening)

    suspend fun updateSeller(token: String,
                            noRekening: String,): LiveData<Result<RegisSellerResponse>> =
        userRepository.updateSeller(token, noRekening)
    suspend fun getPenjualByEmail(token: String, email: String): LiveData<GetPenjualEmailResponse> =
        userRepository.getPenjualByEmail(token, email)
    fun checkIfTokenAvailable(): LiveData<String?> = userRepository.getToken()

    fun logout() {
        viewModelScope.launch(Dispatchers.IO) {
            authPreferences.deleteToken()
        }
    }
}