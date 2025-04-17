package com.example.clm_iot2.ui.singleorder

import ProductRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clm_iot2.model.Order
import com.example.clm_iot2.model.OrderProduct
import com.example.clm_iot2.model.SingleOrderResponse
import com.example.clm_iot2.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleOrderViewModel(
    private val orderRepository: OrderRepository = OrderRepository(),
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    // LiveData pour exposer le résultat de la récupération des détails d'une seule commande
    private val _orderDetails = MutableLiveData<Result<SingleOrderResponse>>()
    val orderDetails: LiveData<Result<SingleOrderResponse>> get() = _orderDetails

    private val _productDetails = MutableLiveData<Result<List<OrderProduct>>>()
    val productDetails: LiveData<Result<List<OrderProduct>>> get() = _productDetails

    // Fonction pour récupérer les détails d'une commande spécifique
    fun getOrderDetails(orderId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = orderRepository.getOrderDetails(orderId)
            _orderDetails.postValue(result) // Mise à jour du LiveData avec le résultat
        }
    }

    fun fetchProductsByIds(ids: List<Int>) {
        // Appel asynchrone en utilisant un callback
        productRepository.getProductsByIds(ids) { result ->
            // L'appel a répondu, maintenant on poste le résultat
            _productDetails.postValue(result)
        }
    }


}
