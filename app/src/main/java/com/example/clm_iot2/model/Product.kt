package com.example.clm_iot2.model

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    var quantity: Int = 1,

    var availableStock: Int = Int.MAX_VALUE,

    @SerializedName("stockId")
    val stock: Int,

    val imageUrl: String?
)

data class StockResponse (
    val id: Int,
    val productId: Int,
    val quantity: Int,
    val stockMin: Int,
    val stockMax: Int,
    val lastUpdated: String
)
