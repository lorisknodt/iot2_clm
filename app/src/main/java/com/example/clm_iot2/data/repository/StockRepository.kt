package com.example.clm_iot2.data.repository;

import RetrofitInstance

class StockRepository {

    suspend fun getAvailableStock(productId: Int): Int {
        return try {
            val stock = RetrofitInstance.api.getStockByProductId(productId)
            stock.quantity
        } catch (e: Exception) {
            e.printStackTrace()
            Int.MAX_VALUE // Fail-safe : on retourne un très grand nombre pour ne pas bloquer l’UI
        }
    }
}