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

        Log.e("PaymentFragment", "Voici le status: ${status} et l'orderid: ${orderId}")
        when (status) {
            "success" -> {
                Toast.makeText(requireContext(), "Paiement réussi !", Toast.LENGTH_LONG).show()
                // TODO: mettre à jour l'état de la commande
            }
            "error" -> {
                Toast.makeText(requireContext(), "Échec du paiement", Toast.LENGTH_LONG).show()
            }
        }

        findNavController().popBackStack() // Revenir à l'écran précédent
    }
}
