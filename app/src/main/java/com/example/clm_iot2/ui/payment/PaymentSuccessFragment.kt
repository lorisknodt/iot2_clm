package com.example.clm_iot2.ui.payment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clm_iot2.R

class PaymentSuccessFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_success, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)


        // Récupérer les références des vues
        val tvOrderNumber = view.findViewById<TextView>(R.id.tvOrderNumber)
        val btnContinueShopping = view.findViewById<Button>(R.id.btnContinueShopping)
        val btnViewOrder = view.findViewById<Button>(R.id.btnViewOrder)

        val orderId = arguments?.getInt("orderId")
        orderId?.let {
            tvOrderNumber.text = getString(R.string.order_number_format, it.toString())
        }

        // Configurer le numéro de commande (option si vous passez des arguments)
        // Si vous n'avez pas besoin d'arguments, vous pouvez juste garder le texte par défaut dans le XML
        try {
            // Si vous avez des arguments (facultatif)
            // val args = arguments
            // val orderNumber = args?.getString("orderNumber", "00000")
            // tvOrderNumber.text = getString(R.string.order_number_format, orderNumber)
        } catch (e: Exception) {
            // Conserver le texte par défaut du XML
        }

        // Configurer les boutons
        btnContinueShopping.setOnClickListener {
            // Naviguer vers la page d'accueil
            findNavController().navigate(R.id.action_paymentSuccessFragment_to_productFragment)
        }

        btnViewOrder.setOnClickListener {
            // Naviguer vers la page des commandes
            findNavController().navigate(R.id.action_paymentSuccessFragment_to_ordersFragment)
        }
    }
}