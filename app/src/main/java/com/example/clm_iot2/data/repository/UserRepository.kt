package com.example.clm_iot2.data

import com.example.clm_iot2.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository {

    suspend fun getUserProfile(userId: Int): User {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getUserDetails(userId)
        }
    }

    suspend fun logout() {
        withContext(Dispatchers.IO) {
            // Logique future de logout
        }
    }
}
