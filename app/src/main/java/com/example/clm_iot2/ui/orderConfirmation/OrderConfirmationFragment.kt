package com.example.clm_iot2.ui.orderConfirmation

import CartViewModel
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Address
import com.example.clm_iot2.model.NewAddress
import com.example.clm_iot2.model.OrderItem
import com.example.clm_iot2.model.OrderRequest
import com.example.clm_iot2.repository.OrderRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderConfirmationFragment : Fragment() {

    private lateinit var orderRepository: OrderRepository
    private lateinit var loadingCard: androidx.cardview.widget.CardView

    // Utilisation de activityViewModels pour partager le ViewModel avec CartFragment
    private val cartViewModel: CartViewModel by activityViewModels()

    private val orderConfViewModel: OrderConfirmationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser la Progressbar
        loadingCard = view.findViewById(R.id.loadingCard)
        val baseLayout = view.findViewById<View>(R.id.baseLayout)
        val addressLoadingProgressBar = view.findViewById<ProgressBar>(R.id.addressLoadingProgressBar)


        // Initialiser le repository
        orderRepository = OrderRepository()

        // Initialiser la dropdown
        val addressDropdown = view.findViewById<AutoCompleteTextView>(R.id.addressDropdown)
        val addressDropdownLayout = view.findViewById<View>(R.id.addressDropdownLayout)

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
            loadingCard.visibility = View.VISIBLE
            baseLayout.visibility = View.GONE

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
                loadingCard.visibility = View.GONE
                baseLayout.visibility = View.VISIBLE

                if (success) {
                    // Succès : on affiche un message, redirige et vide le panier
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

                    // Vider le panier
                    cartViewModel.clearCart()

                    // TODO :aller vers un écran de recapitulatif et remerciement
                    // findNavController().navigate(R.id.action_to_orderSuccessFragment)

                } else {
                    // Échec : on affiche le message d’erreur
                    Toast.makeText(requireContext(), "Erreur : $message", Toast.LENGTH_LONG).show()
                }
            }

        }


        // Fonction pour formater une adresse complète
        fun formatAddress(address: Address): String {
            return listOfNotNull(
                address.street,
                address.number.takeIf { it.isNotEmpty() },
                address.city,
                address.zipCode,
                address.country
            ).joinToString(", ")
        }


        fun fillAddressFields(address: Address) {
            editTextStreet.setText(address.street)
            editTextApartment.setText(address.number)
            editTextCity.setText(address.city)
            editTextCountry.setText(address.country)
            editTextPostalCode.setText(address.zipCode)
        }


        // Initialisation du comportement replier/déplier
        val headerShippingAddress = view.findViewById<LinearLayout>(R.id.headerShippingAddress)
        val containerAddressFields = view.findViewById<LinearLayout>(R.id.containerAddressFields)
        val imgExpandCollapse = view.findViewById<ImageView>(R.id.imgExpandCollapse)

        headerShippingAddress.setOnClickListener {
            // Basculer la visibilité des champs d'adresse
            if (containerAddressFields.visibility == View.VISIBLE) {
                containerAddressFields.visibility = View.GONE
                imgExpandCollapse.setImageResource(R.drawable.ic_expand_more) // Changer l'icône
            } else {
                containerAddressFields.visibility = View.VISIBLE
                imgExpandCollapse.setImageResource(R.drawable.ic_expand_less) // Changer l'icône
            }
        }


        // Appel à l’API pour récupérer les adresses du client
        addressDropdownLayout.visibility = View.INVISIBLE // On garde l'espace mais on masque visuellement
        addressLoadingProgressBar.visibility = View.VISIBLE


        lifecycleScope.launch(Dispatchers.IO) {
            val addressList = orderConfViewModel.fetchAddresses(1) // 🔁 Remplace par l’ID réel du client

            val addressMap = addressList.associateBy { formatAddress(it) }
            val formattedList = addressMap.keys.toList()

            launch(Dispatchers.Main) {
                val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, formattedList)

                addressDropdownLayout.visibility = View.VISIBLE
                addressLoadingProgressBar.visibility = View.GONE

                addressDropdown.setAdapter(adapter)

                addressDropdown.setOnItemClickListener { _, _, position, _ ->
                    val selectedLabel = formattedList[position]
                    val selectedAddress = addressMap[selectedLabel]
                    selectedAddress?.let {
                        fillAddressFields(it)
                    }
                }
            }
        }

    }
}
