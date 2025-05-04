package com.example.clm_iot2.ui.orderConfirmation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.clm_iot2.R
import com.example.clm_iot2.data.repository.AddressRepository
import com.example.clm_iot2.model.Address

class OrderConfirmationViewModel : ViewModel() {

    private val repository = AddressRepository()

    private val _addresses = MutableLiveData<List<Address>?>()
    val addresses: MutableLiveData<List<Address>?> = _addresses

    // Nouvelles propriétés pour la gestion de l'option "En cas d'absence"
    private val _selectedDeliveryOption = MutableLiveData<String>()
    val selectedDeliveryOption: LiveData<String> = _selectedDeliveryOption

    private val _customDeliveryOption = MutableLiveData<String>()
    val customDeliveryOption: LiveData<String> = _customDeliveryOption

    // Constantes pour les options de livraison
    companion object {
        const val OPTION_GARDEN = "GARDEN"
        const val OPTION_CARPORT = "CARPORT"
        const val OPTION_NEIGHBOR = "NEIGHBOR"
        const val OPTION_BACK_DOOR = "BACK_DOOR"
        const val OPTION_OTHER = "OTHER"
        val OPTION_NONE = null
    }

    suspend fun fetchAddresses(clientId: String): List<Address> {
        val result = repository.getAddresses(clientId)
        return if (result.isSuccess) {
            result.getOrNull() ?: emptyList()
        } else {
            emptyList()
        }
    }

    // Méthode pour définir l'option de livraison sélectionnée
    fun setSelectedDeliveryOption(option: String) {
        _selectedDeliveryOption.value = option
    }

    // Méthode pour définir le texte personnalisé de l'option "Autre"
    fun setCustomDeliveryOption(text: String) {
        _customDeliveryOption.value = text
    }

    // Méthode pour effacer l'option sélectionnée
    fun clearSelectedDeliveryOption() {
        _selectedDeliveryOption.value = OPTION_NONE
    }

    // Méthode pour obtenir les informations complètes de livraison en cas d'absence
    fun getAbsenceDeliveryInstructions(context: Context): String {
        return when (_selectedDeliveryOption.value) {
            OPTION_GARDEN -> context.getString(R.string.option_garden)
            OPTION_CARPORT -> context.getString(R.string.option_carport)
            OPTION_NEIGHBOR -> context.getString(R.string.option_neighbor)
            OPTION_BACK_DOOR -> context.getString(R.string.option_back_door)
            OPTION_OTHER -> context.getString(R.string.option_other, _customDeliveryOption.value ?: "")
            else -> ""
        }
    }
}