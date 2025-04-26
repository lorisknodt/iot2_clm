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
    val apiService = RetrofitInstance.getApi()

    // Fonction pour créer la commande
    fun createOrder(orderRequest: OrderRequest, callback: (Boolean, String, Long?) -> Unit) {
        apiService.createOrder(orderRequest)
            .enqueue(object : Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: Response<OrderResponse>) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        // La commande a été créée avec succès
                        callback(true, "Commande créée avec succès: ${responseBody?.id}", responseBody?.id)
                    } else {
                        // Si la réponse est une erreur
                        callback(false, "Erreur lors de la création de la commande, ${response}", null)
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    // Si la requête échoue (erreur réseau)
                    callback(false, "Erreur réseau", null)
                }
            })
    }

    // Nouvelle fonction pour récupérer les commandes
    suspend fun getOrders(): Result<List<Order>> {
        return withContext(Dispatchers.IO) {
            try {
                val orders = apiService.getOrdersByClient()
                Result.success(orders)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    suspend fun getOrderDetails(orderId: Int): Result<SingleOrderResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val orderEntity = apiService.getOrderById(orderId)
                Result.success(orderEntity)  // Retourne l'objet Order en cas de succès
            } catch (e: Exception) {
                Result.failure<SingleOrderResponse>(e)  // Retourne une erreur en cas d'exception
            }
        }
    }

    fun updateOrderStatus(orderId: Long, newStatus: String, callback: (success: Boolean, message: String) -> Unit) {
        apiService.updateOrderStatus(orderId, newStatus)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        callback(true, "Statut mis à jour")
                    } else {
                        callback(false, "Erreur lors de la mise à jour du statut")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    callback(false, "Erreur réseau : ${t.message}")
                }
            })
    }

    fun deleteOrder(orderId: Long, callback: (success: Boolean, message: String) -> Unit) {
        apiService.deleteOrder(orderId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        callback(true, "Commande supprimée avec succès")
                    } else {
                        callback(false, "Erreur lors de la suppression de la commande")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    callback(false, "Erreur réseau : ${t.message}")
                }
            })
    }


}
