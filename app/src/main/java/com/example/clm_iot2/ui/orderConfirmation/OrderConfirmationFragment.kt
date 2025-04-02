package com.example.clm_iot2.ui.orderConfirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.clm_iot2.R
import com.example.clm_iot2.model.NewAddress
import com.example.clm_iot2.model.OrderItem
import com.example.clm_iot2.model.OrderRequest
import com.example.clm_iot2.repository.OrderRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class OrderConfirmationFragment : Fragment() {

    private lateinit var orderRepository: OrderRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser le repository
        orderRepository = OrderRepository(requireContext())

        // Champs de l'adresse
        val editTextStreet = view.findViewById<TextInputEditText>(R.id.editTextStreet)
        val editTextApartment = view.findViewById<TextInputEditText>(R.id.editTextApartment)
        val editTextCity = view.findViewById<TextInputEditText>(R.id.editTextCity)
        val editTextCountry = view.findViewById<TextInputEditText>(R.id.editTextCountry)
        val editTextPostalCode = view.findViewById<TextInputEditText>(R.id.editTextPostalCode)

        // Champs de la carte bancaire
        val editTextCardName = view.findViewById<TextInputEditText>(R.id.editTextCardName)
        val editTextCardNumber = view.findViewById<TextInputEditText>(R.id.editTextCardNumber)
        val editTextExpiryDate = view.findViewById<TextInputEditText>(R.id.editTextExpiryDate)
        val editTextCVV = view.findViewById<TextInputEditText>(R.id.editTextCVV)

        val btnConfirmOrder = view.findViewById<MaterialButton>(R.id.btnConfirmOrder)

        btnConfirmOrder.setOnClickListener {
            val street = editTextStreet.text.toString().trim()
            val apartment = editTextApartment.text.toString().trim()
            val city = editTextCity.text.toString().trim()
            val country = editTextCountry.text.toString().trim()
            val postalCode = editTextPostalCode.text.toString().trim()

            // Vérification des champs obligatoires (adresse)
            if (street.isEmpty() || city.isEmpty() || country.isEmpty() || postalCode.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez remplir tous les champs obligatoires de l'adresse.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Création de l'objet OrderRequest
            val orderRequest = OrderRequest(
                orderItemsCreateDTOList = listOf(OrderItem(9007199254740991, 1073741824)),
                adresseId = 5,
                newAddress = NewAddress(
                    street = street,
                    city = city,
                    number = apartment, // Utilisation du champ "apartment" pour "number"
                    zipCode = postalCode,
                    country = country
                )
            )

            // Appel au repository pour créer la commande
            /*orderRepository.createOrder(orderRequest) { success, message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }*/
        }
    }
}
