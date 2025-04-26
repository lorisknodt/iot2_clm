package com.example.clm_iot2.model

data class OrderRequest(
    val orderItemsCreateDTOList: List<OrderItem>,
    val adresseId: Int?,
    val newAddress: NewAddress,
    val deliveryInstruction: String?
)

data class OrderResponse(
    val id: Long,
    val status: String,
    val message: String
)

data class SingleOrderResponse(
    val id: Int,
    val orderDate: String,
    val orderTime: String,
    val status: String,
    val bankName: String,
    val total: Double,
    val orderItems: List<OrderItem>,
    val address: Address,
    val clientId: Int
)

data class OrderItem(
    val productId: Int,
    val quantity: Int
)

data class OrderProduct(
    val id: Int,
    val name: String,
    val price: Double,
    val description: String,
    val category: Category,
    val quantity: Int,
    val imageResId: Int
)

data class NewAddress(
    val street: String,
    val city: String,
    val number: String,
    val zipCode: String,
    val country: String,
    val deliveryInstructions: String? = null
)
