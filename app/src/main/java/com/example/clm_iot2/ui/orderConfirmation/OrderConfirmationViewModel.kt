package com.example.clm_iot2.ui.orderConfirmation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clm_iot2.data.repository.AddressRepository
import com.example.clm_iot2.model.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderConfirmationViewModel : ViewModel() {

    private val repository = AddressRepository()

    private val _addresses = MutableLiveData<List<Address>?>()
    val addresses: MutableLiveData<List<Address>?> = _addresses

    suspend fun fetchAddresses(clientId: Int): List<Address> {
        val result = repository.getAddresses(clientId)
        return if (result.isSuccess) {
            result.getOrNull() ?: emptyList()
        } else {
            emptyList()
        }
    }

}