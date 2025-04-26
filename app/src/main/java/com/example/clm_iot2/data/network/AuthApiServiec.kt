package com.example.clm_iot2.data.network

import com.example.clm_iot2.model.LoginRequest
import com.example.clm_iot2.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import com.example.clm_iot2.model.AccountRequest as AccountRequest1

interface AuthApiService {
    @POST("accounts")
    suspend fun createAccount(@Body account: AccountRequest1): Response<Void>

    @POST("auth/login")
    suspend fun login(@Body login : LoginRequest): Response<LoginResponse>

}