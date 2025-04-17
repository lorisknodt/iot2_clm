package com.example.clm_iot2.ui.orders

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clm_iot2.model.Order
import com.example.clm_iot2.repository.OrderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrdersViewModel : ViewModel() {

    private val orderRepository = OrderRepository()

    private val _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>> = _orders

    init {
        fetchOrders()
    }

    fun fetchOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = orderRepository.getOrders()
            result.onSuccess { ordersList ->
                _orders.postValue(ordersList)
            }.onFailure { e ->
                // Gérer l'erreur
                _orders.postValue(emptyList())
            }
        }
    }
}
