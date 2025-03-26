package com.example.clm_iot2.ui.orderConfirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.clm_iot2.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText

class OrderConfirmationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_confirmation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editTextAddress = view.findViewById<TextInputEditText>(R.id.editTextAddress)
        val btnConfirmOrder = view.findViewById<MaterialButton>(R.id.btnConfirmOrder)

        val radioButtonCard = view.findViewById<MaterialRadioButton>(R.id.radioButtonCard)
        val radioButtonPaypal = view.findViewById<MaterialRadioButton>(R.id.radioButtonPaypal)

        val allPaymentButtons = listOf(radioButtonCard, radioButtonPaypal)

        // Gestion de la sélection unique entre les boutons
        allPaymentButtons.forEach { radioButton ->
            radioButton.setOnClickListener {
                allPaymentButtons.forEach { it.isChecked = false } // On désélectionne tout
                radioButton.isChecked = true                       // Puis on sélectionne le bouton cliqué
            }
        }

        btnConfirmOrder.setOnClickListener {
            val address = editTextAddress.text.toString()

            if (address.isEmpty()) {
                Toast.makeText(requireContext(), "Veuillez entrer une adresse", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedPaymentMethod = when {
                radioButtonCard.isChecked -> "Carte bancaire"
                radioButtonPaypal.isChecked -> "PayPal"
                else -> {
                    Toast.makeText(requireContext(), "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            Toast.makeText(
                requireContext(),
                "Commande validée avec :\nAdresse: $address\nPaiement: $selectedPaymentMethod",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
