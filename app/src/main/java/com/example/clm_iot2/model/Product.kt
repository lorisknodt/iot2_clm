package com.example.clm_iot2.model

import com.google.gson.annotations.SerializedName

data class Product (
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val price: Double,

    // L'API ne fournit pas d'icône, donc garde cette propriété en tant qu'attribut local
    val iconeResId: Int = 0,

    var quantity: Int = 1,

    var availableStock: Int = Int.MAX_VALUE,

    @SerializedName("stockId")
    val stock: Int
)

data class StockResponse (
    val id: Int,
    val productId: Int,
    val quantity: Int,
    val stockMin: Int,
    val stockMax: Int,
    val lastUpdated: String
)
