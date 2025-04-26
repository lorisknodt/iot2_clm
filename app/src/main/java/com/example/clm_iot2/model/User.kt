package com.example.clm_iot2.model

data class User(
    var id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: Address?,
    val registrationDate: String,
    val clientAccountNumber: String,
    val sex: String?,
    val birthday: String?,
    val maritalStatus: String?,
    val monthlySalary: Int?,
    val role: String
)