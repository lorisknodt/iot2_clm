package com.example.clm_iot2.model

data class OrderRequest(
    val orderItemsCreateDTOList: List<OrderItem>,
    val adresseId: Int?,
    val newAddress: NewAddress
)

data class OrderResponse(
    val orderId: Long,
    val status: String,
    val message: String
)

data class OrderItem(
    val productId: Int,
    val quantity: Int
)

data class NewAddress(
    val street: String,
    val city: String,
    val number: String,
    val zipCode: String,
    val country: String
)
