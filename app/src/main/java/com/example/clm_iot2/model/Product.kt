package com.example.clm_iot2.model

import com.google.gson.annotations.SerializedName

data class Product (
    val id: Int,

    @SerializedName("name")
    val nom: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("price")
    val prix: Double,

    // L'API ne fournit pas d'icône, donc garde cette propriété en tant qu'attribut local
    val iconeResId: Int = 0,

    var quantity: Int = 1,

    @SerializedName("stockId")
    val stock: Int
)
