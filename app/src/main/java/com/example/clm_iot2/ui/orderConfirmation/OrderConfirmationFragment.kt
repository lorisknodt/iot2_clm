package com.example.clm_iot2.ui.orderConfirmation

import CartViewModel
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Address
import com.example.clm_iot2.model.NewAddress
import com.example.clm_iot2.model.OrderItem
import com.example.clm_iot2.model.OrderRequest
import com.example.clm_iot2.repository.OrderRepository
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderConfirmationFragment : Fragment() {

    private lateinit var orderRepository: OrderRepository
    private lateinit var loadingCard: androidx.cardview.widget.CardView

    // Champs pour les options en cas d'absence
    private lateinit var radioGroupDeliveryOptions: RadioGroup
    private lateinit var radioButtonOther: MaterialRadioButton
    private lateinit var otherDeliveryOptionLayout: TextInputLayout
    private lateinit var editTextOtherOption: TextInputEditText
    private lateinit var textViewCharCount: TextView

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

        // Initialisation des éléments pour les options en cas d'absence
        radioGroupDeliveryOptions = view.findViewById(R.id.radioGroupDeliveryOptions)
        radioButtonOther = view.findViewById(R.id.radioButtonOther)
        otherDeliveryOptionLayout = view.findViewById(R.id.otherDeliveryOptionLayout)
        editTextOtherOption = view.findViewById(R.id.editTextOtherOption)
        textViewCharCount = view.findViewById(R.id.textViewCharCount)

        // Configurer le RadioGroup pour les options de livraison
        setupDeliveryOptions()

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
                loadingCard.visibility = View.GONE
                baseLayout.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Vérification qu'une option de livraison en cas d'absence est sélectionnée
            if (radioGroupDeliveryOptions.checkedRadioButtonId == -1) {
                Toast.makeText(requireContext(), "Veuillez sélectionner une option de livraison en cas d'absence.", Toast.LENGTH_SHORT).show()
                loadingCard.visibility = View.GONE
                baseLayout.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Vérification que le champ texte est rempli pour l'option "Autre"
            if (radioButtonOther.isChecked && editTextOtherOption.text.toString().trim().isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez préciser l'option 'Autre'.", Toast.LENGTH_SHORT).show()
                loadingCard.visibility = View.GONE
                baseLayout.visibility = View.VISIBLE
                return@setOnClickListener
            }

            // Récupération des articles du panier depuis le ViewModel
            val cartItems = cartViewModel.getCartProducts()

            // Création de la liste des items pour la commande
            val orderItems = cartItems.map { item ->
                OrderItem(item.id, item.quantity)
            }

            // Récupération des instructions de livraison en cas d'absence
            val absenceInstructions = orderConfViewModel.getAbsenceDeliveryInstructions(requireContext())

            // Log pour vérification
            Log.d("OrderConfirmation", "Instructions en cas d'absence: $absenceInstructions")

            // Création de l'objet OrderRequest avec les données dynamiques du panier
            val orderRequest = OrderRequest(
                orderItemsCreateDTOList = orderItems,
                adresseId = null,  // Supposition parce que l'instance existe pas encore je crois
                newAddress = NewAddress(
                    street = street,
                    city = city,
                    number = apartment,
                    zipCode = postalCode,
                    country = country,
                    // Ajoutez ici les instructions de livraison en cas d'absence si votre API les prend en charge
                    // deliveryInstructions = absenceInstructions
                ),
                deliveryInstruction = orderConfViewModel.selectedDeliveryOption.toString()
            )

            // Affichage des produits dans les logs pour tester
            cartItems.forEach { item ->
                Log.d("OrderConfirmation", "Product-id: ${item.id}, Produit: ${item.name}, Quantité: ${item.quantity}, Prix: ${item.price}")
            }

            // Appel au repository pour créer la commande
            orderRepository.createOrder(orderRequest) { success, message, orderId ->
                loadingCard.visibility = View.GONE
                baseLayout.visibility = View.VISIBLE

                if (success) {
                    // Succès : on affiche un message, redirige et vide le panier
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

                    // Vider le panier
                    cartViewModel.clearCart()

                    // Rediriger vers l'écran de paiement
                    val amount = cartItems.sumOf { it.price * it.quantity }

                    val bundle = Bundle().apply {
                        if (orderId != null) {
                            putInt("orderId", orderId.toInt())
                        }
                        putDouble("amount", amount)
                    }

                    findNavController().navigate(
                        R.id.action_orderConfirmation_to_paymentFragment,
                        bundle
                    )

                } else {
                    // Échec : on affiche le message d'erreur
                    Toast.makeText(requireContext(), "Erreur : $message", Toast.LENGTH_LONG).show()
                    Log.d("OrderConfirmationFragment", "Voici l'erreur: $message")
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

        // Appel à l'API pour récupérer les adresses du client
        addressDropdownLayout.visibility = View.INVISIBLE // On garde l'espace mais on masque visuellement
        addressLoadingProgressBar.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            val addressList = orderConfViewModel.fetchAddresses(1) // TODO Remplacer par l'ID réel du client quand on aura le token

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

    // Méthode pour configurer les options de livraison en cas d'absence
    private fun setupDeliveryOptions() {
        // Variable pour suivre le RadioButton actuellement sélectionné
        var previousCheckedId: Int = -1

        // Pour chaque RadioButton dans le groupe
        for (i in 0 until radioGroupDeliveryOptions.childCount) {
            val radioButton = radioGroupDeliveryOptions.getChildAt(i) as? MaterialRadioButton
            radioButton?.setOnClickListener { view ->
                val clickedRadioButton = view as MaterialRadioButton

                // Si ce RadioButton était déjà sélectionné, le décocher
                if (clickedRadioButton.id == previousCheckedId) {
                    radioGroupDeliveryOptions.clearCheck() // Décocher tous les boutons
                    previousCheckedId = -1
                    // Mettre à jour le ViewModel
                    orderConfViewModel.clearSelectedDeliveryOption()
                    otherDeliveryOptionLayout.visibility = View.GONE
                } else {
                    // Sinon, enregistrer ce RadioButton comme étant sélectionné
                    previousCheckedId = clickedRadioButton.id

                    // Appliquer la logique existante en fonction du bouton sélectionné
                    when (clickedRadioButton.id) {
                        R.id.radioButtonGarden -> {
                            orderConfViewModel.setSelectedDeliveryOption(OrderConfirmationViewModel.OPTION_GARDEN)
                            otherDeliveryOptionLayout.visibility = View.GONE
                        }
                        R.id.radioButtonCarport -> {
                            orderConfViewModel.setSelectedDeliveryOption(OrderConfirmationViewModel.OPTION_CARPORT)
                            otherDeliveryOptionLayout.visibility = View.GONE
                        }
                        R.id.radioButtonNeighbor -> {
                            orderConfViewModel.setSelectedDeliveryOption(OrderConfirmationViewModel.OPTION_NEIGHBOR)
                            otherDeliveryOptionLayout.visibility = View.GONE
                        }
                        R.id.radioButtonBackDoor -> {
                            orderConfViewModel.setSelectedDeliveryOption(OrderConfirmationViewModel.OPTION_BACK_DOOR)
                            otherDeliveryOptionLayout.visibility = View.GONE
                        }
                        R.id.radioButtonOther -> {
                            orderConfViewModel.setSelectedDeliveryOption(OrderConfirmationViewModel.OPTION_OTHER)
                            otherDeliveryOptionLayout.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }

        // Gestion du compteur de caractères pour l'option "Autre"
        editTextOtherOption.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentLength = s?.length ?: 0
                textViewCharCount.text = "$currentLength/50"
                orderConfViewModel.setCustomDeliveryOption(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}