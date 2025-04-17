package com.example.clm_iot2.repository

import android.content.Context
import com.example.clm_iot2.model.Order
import com.example.clm_iot2.model.OrderRequest
import com.example.clm_iot2.model.OrderResponse
import com.example.clm_iot2.model.SingleOrderResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository{

    // Fonction pour créer la commande
    fun createOrder(orderRequest: OrderRequest, callback: (Boolean, String) -> Unit) {
        RetrofitInstance.api.createOrder(orderRequest)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                    if (response.isSuccessful) {
                        // La commande a été créée avec succès
                        callback(true, "Commande créée avec succès")
                    } else {
                        // Si la réponse est une erreur
                        callback(false, "Erreur lors de la création de la commande")
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    // Si la requête échoue (erreur réseau)
                    callback(false, "Erreur réseau")
                }
            })
    }

    // Nouvelle fonction pour récupérer les commandes
    suspend fun getOrders(): Result<List<Order>> {
        return withContext(Dispatchers.IO) {
            try {
                val orders = RetrofitInstance.api.getOrders()  // Ici, APIService doit fournir une méthode suspendue : suspend fun getOrders(): Response<List<Order>> ou directement List<Order>
                Result.success(orders)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    suspend fun getOrderDetails(orderId: Int): Result<SingleOrderResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val orderEntity = RetrofitInstance.api.getOrderById(orderId)
                Result.success(orderEntity)  // Retourne l'objet Order en cas de succès
            } catch (e: Exception) {
                Result.failure<SingleOrderResponse>(e)  // Retourne une erreur en cas d'exception
            }
        }
    }

}
