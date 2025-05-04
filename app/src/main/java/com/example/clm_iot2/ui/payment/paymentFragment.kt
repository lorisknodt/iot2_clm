package com.example.clm_iot2.ui.payment

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.clm_iot2.R
import com.example.clm_iot2.repository.OrderRepository
import java.net.URLEncoder

class PaymentFragment : Fragment() {

    private lateinit var webView: WebView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        webView = view.findViewById(R.id.paymentWebView)


        val orderId = arguments?.getInt("orderId") ?: return
        val amount = arguments?.getDouble("amount") ?: return
        val returnUrl = URLEncoder.encode("myapp://payment-result", "UTF-8") //Ce que le site va utiliser pour rediriger

        val paymentUrl = "https://payment.masmzi.icu/?" +
                "orderId=$orderId&" +
                "amount=$amount&" +
                "returnUrl=$returnUrl&"
        Log.d("PaymentFragment", "voici l'url: ${paymentUrl}")

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {

            // Gestion du résultat du payement
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let {
                    if (it.startsWith("myapp://payment-result")) {
                        handlePaymentResult(Uri.parse(it))
                        return true
                    }
                }
                return false
            }
        }

        webView.loadUrl(paymentUrl)
    }

    private fun handlePaymentResult(uri: Uri) {
        val status = uri.getQueryParameter("status")
        val orderId = uri.getQueryParameter("orderId")?.toIntOrNull()

        Log.e("PaymentFragment", "Voici le status: $status et l'orderid: $orderId")

        when (status) {
            "success" -> {
                Toast.makeText(requireContext(), "Paiement réussi !", Toast.LENGTH_LONG).show()

                orderId?.let { id ->
                    OrderRepository().updateOrderStatus(id.toLong(), "confirmed") { success, message ->
                        if (success) {
                            Log.d("PaymentFragment", "Statut mis à jour avec succès")
                            val bundle = Bundle().apply {
                                putInt("orderId", id) // ou id.toLong() si tu préfères
                            }
                            findNavController().navigate(R.id.action_paymentFragment_to_paymentSuccessFragment, bundle)

                        } else {
                            Log.e("PaymentFragment", "Échec de mise à jour du statut: $message")
                            Toast.makeText(requireContext(), "Erreur serveur. Réessayez.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            "error" -> {
                Toast.makeText(requireContext(), "Échec du paiement", Toast.LENGTH_LONG).show()

                //findNavController().navigate(R.id.action_paymentFragment_to_paymentFailedFragment)

                orderId?.let { id ->
                    OrderRepository().updateOrderStatus(id.toLong(), "cancelled") { success, message ->
                        if (success) {
                            Log.d("PaymentFragment", "Commande annulée avec succès")
                            findNavController().navigate(R.id.action_paymentFragment_to_paymentFailedFragment)
                        } else {
                            Log.e("PaymentFragment", "Erreur d'annulation de la commande: $message")
                            Toast.makeText(requireContext(), "Erreur serveur. Réessayez.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}
