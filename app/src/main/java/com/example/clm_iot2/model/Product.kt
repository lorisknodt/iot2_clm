package com.example.clm_iot2.model

data class Product (
    val id: Int,
    val nom: String,
    val description: String,
    val prix: Double,
    val iconeResId: Int,
    var quantity: Int = 1,
    val stock: Int
)