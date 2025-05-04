package com.example.clm_iot2.data.repository

import RetrofitInstance
import ApiService
import com.example.clm_iot2.model.Address

class AddressRepository(private val api: ApiService = RetrofitInstance.api) {

    suspend fun getAddresses(clientId: Int): Result<List<Address>> {
        return try {
            val addresses = api.getAddresses(clientId)
            Result.success(addresses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}