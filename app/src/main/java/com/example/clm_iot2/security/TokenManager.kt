package com.example.clm_iot2.security

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object TokenManager {

    private var sharedPreferences: SharedPreferences? = null

    fun init(context: Context) {
        if (sharedPreferences == null) {
            val appContext = context.applicationContext

            val masterKey = MasterKey.Builder(appContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            sharedPreferences = EncryptedSharedPreferences.create(
                appContext,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        }
    }

    fun saveToken(token: String) {
        if (sharedPreferences == null) {
            throw IllegalStateException("TokenManager is not initialized. Call TokenManager.init(context) first.")
        }
        sharedPreferences?.edit()?.putString("auth_token", token)?.apply()
    }

    fun getToken(): String? {
        if (sharedPreferences == null) {
            throw IllegalStateException("TokenManager is not initialized. Call TokenManager.init(context) first.")
        }
        return sharedPreferences?.getString("auth_token", null)
    }

    fun clearToken() {
        if (sharedPreferences == null) {
            throw IllegalStateException("TokenManager is not initialized. Call TokenManager.init(context) first.")
        }
        sharedPreferences?.edit()?.remove("auth_token")?.apply()
    }
}
