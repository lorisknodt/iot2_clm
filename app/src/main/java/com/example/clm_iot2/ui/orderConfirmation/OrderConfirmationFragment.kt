package com.example.clm_iot2.ui.orderConfirmation

import CartViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.clm_iot2.R
import com.example.clm_iot2.model.NewAddress
import com.example.clm_iot2.model.OrderItem
import com.example.clm_iot2.model.OrderRequest
import com.example.clm_iot2.repository.OrderRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class OrderConfirmationFragment : Fragment() {

    private lateinit var orderRepository: OrderRepository

    // Utilisation de activityViewModels pour partager le ViewModel avec CartFragment
    private val cartViewModel: CartViewModel by activityViewModels()

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

            // Récupération des articles du panier depuis le ViewModel
            val cartItems = cartViewModel.getCartProducts()

            // Création de la liste des items pour la commande
            val orderItems = cartItems.map { item ->
                OrderItem(item.id, item.quantity)
            }

            // Création de l'objet OrderRequest avec les données dynamiques du panier
            val orderRequest = OrderRequest(
                orderItemsCreateDTOList = orderItems,
                adresseId = null,  // Supposition parce que l'instance existe pas encore je crois
                newAddress = NewAddress(
                    street = street,
                    city = city,
                    number = apartment, // Utilisation du champ "apartment" pour "number"
                    zipCode = postalCode,
                    country = country
                )
            )
            // Récupération des articles du panier depuis le ViewModel

            // Affichage des produits dans les logs pour tester
            cartItems.forEach { item ->
                Log.d("OrderConfirmation", "Product-id: ${item.id}, Produit: ${item.nom}, Quantité: ${item.quantity}, Prix: ${item.prix}")
            }

            // Appel au repository pour créer la commande
            orderRepository.createOrder(orderRequest) { success, message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
