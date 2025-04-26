package com.example.clm_iot2.model

data class LoginResponse(
    val token: String,
    val requiresSecondFactor: Boolean,
    val clientId: String
)

data class LoginRequest(
    val email: String,
    val password: String,
)

data class AccountRequest(
    val clientName: String,
    val password: String,
    val birthDate: String,
    val email: String,
    val role: String // "CLIENT", "DELIVERY_AGENT", ...
)