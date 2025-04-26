package com.example.clm_iot2.data.repository

import RetrofitInstance
import com.example.clm_iot2.model.Address

class AddressRepository {
    val apiService = RetrofitInstance.getApi()
    suspend fun getAddresses(clientId: Int): Result<List<Address>> {
        return try {
            val addresses = apiService.getAddressesByClient()
            Result.success(addresses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}