package com.example.clm_iot2.data

import com.example.clm_iot2.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {
    val apiService = RetrofitInstance.getApi()

    suspend fun getUserProfile(userId: Int): User {
        return withContext(Dispatchers.IO) {
            apiService.getUserDetails(userId)
        }
    }

    suspend fun getCurrentUserProfile(): User {
        return withContext(Dispatchers.IO) {
            apiService.getCurrentUserDetails()
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            // Logique future de logout
        }
    }
}
