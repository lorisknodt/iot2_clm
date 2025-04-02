package com.example.clm_iot2.repository

import android.content.Context
import android.widget.Toast
import com.example.clm_iot2.model.OrderRequest
import com.example.clm_iot2.model.OrderResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository(private val context: Context) {

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
}
