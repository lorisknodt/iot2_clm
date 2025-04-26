package com.example.clm_iot2.data.network

import android.content.Context
import com.example.clm_iot2.security.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val tokenManager = TokenManager
        val token = tokenManager.getToken()

        val newRequest = if (token != null) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }

        return chain.proceed(newRequest)
    }
}