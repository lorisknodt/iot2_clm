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

class PaymentFailedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment_error, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)


        // Récupérer les références des vues
        val tvErrorCode = view.findViewById<TextView>(R.id.tvErrorCode)
        val btnTryAgain = view.findViewById<Button>(R.id.btnTryAgain)
        val btnExploreProducts = view.findViewById<Button>(R.id.btnExploreProducts2)

        // Configurer le code d'erreur (option si vous passez des arguments)
        // Si vous n'avez pas besoin d'arguments, vous pouvez juste garder le texte par défaut dans le XML
        try {
            // Si vous avez des arguments (facultatif)
            // val args = arguments
            // val errorCode = args?.getString("errorCode", "ERR-UNKNOWN")
            // tvErrorCode.text = getString(R.string.error_code_format, errorCode)
        } catch (e: Exception) {
            // Conserver le texte par défaut du XML
        }

        // Configurer les boutons
        btnTryAgain.setOnClickListener {
            // Naviguer vers la page de paiement pour réessayer
            //findNavController().navigate(R.id.action_paymentFailedFragment_to_paymentFragment)
            findNavController().navigate(R.id.action_paymentFailureFragment_to_developmentFragment)
        }

        btnExploreProducts.setOnClickListener {
            // Naviguer vers la page de produits
            //findNavController().navigate(R.id.action_paymentFailedFragment_to_supportFragment)
            findNavController().navigate(R.id.action_paymentFailureFragment_to_productFragment)
        }
    }
}