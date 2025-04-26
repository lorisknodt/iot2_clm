package com.example.clm_iot2.ui.profile

import androidx.lifecycle.*
import com.example.clm_iot2.data.UserRepository
import com.example.clm_iot2.model.User
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class ProfileViewModel : ViewModel() {

    private val userRepository = UserRepository()

    private val _userProfile = MutableLiveData<User>()
    val userProfile: LiveData<User> = _userProfile

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage


    fun loadUserProfile(userId: Int = 8) {
        viewModelScope.launch {
            try {
                val user = userRepository.getCurrentUserProfile()
                _userProfile.value = user
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors du chargement du profil: ${e.message}"
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logout()
            } catch (e: Exception) {
                _errorMessage.value = "Erreur lors de la déconnexion: ${e.message}"
            }
        }
    }

    fun errorMessageShown() {
        _errorMessage.value = null
    }

    fun formatRegistrationDate(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            date?.let { outputFormat.format(it) } ?: dateString
        } catch (e: Exception) {
            dateString
        }
    }
}
