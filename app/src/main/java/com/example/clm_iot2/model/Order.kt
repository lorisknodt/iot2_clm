package com.example.clm_iot2.model

import java.io.Serializable

data class Order(
    val id: Int,
    val orderDate: String,
    val orderTime: String,
    val status: String,
    val bankName: String,
    val total: Double,
    val address: Address,
    val clientId: Int
) : Serializable

data class Address(
    val id: Int,
    val street: String,
    val city: String,
    val number: String,
    val zipCode: String,
    val country: String
) : Serializable
