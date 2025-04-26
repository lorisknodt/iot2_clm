package com.example.clm_iot2.ui.singleorder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import com.example.clm_iot2.adapter.OrderProductsAdapter
import com.example.clm_iot2.model.OrderProduct
import com.example.clm_iot2.model.SingleOrderResponse
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class SingleOrderFragment : Fragment() {

    private lateinit var singleOrderViewModel: SingleOrderViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingCard: androidx.cardview.widget.CardView
    private lateinit var mainContent: NestedScrollView
    private lateinit var btnCancelOrder: MaterialButton

    private var productQuantities: Map<Int, Int> = emptyMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_single_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderId = arguments?.getInt("orderId") ?: -1

        recyclerView = view.findViewById(R.id.orderProductsList)
        recyclerView.layoutManager = LinearLayoutManager(context)

        loadingCard = view.findViewById(R.id.singleOrderLoadingCard)
        mainContent = view.findViewById(R.id.mainContent)

        btnCancelOrder = view.findViewById(R.id.btnCancelOrder)

        singleOrderViewModel = ViewModelProvider(this).get(SingleOrderViewModel::class.java)



        // Au début : afficher loading
        loadingCard.visibility = View.VISIBLE
        mainContent.visibility = View.GONE

        // Configuration du bouton d'annulation
        btnCancelOrder.setOnClickListener {
            showCancelConfirmationDialog(orderId)
        }

        // Observer pour le statut d'annulation
        singleOrderViewModel.cancelOrderStatus.observe(viewLifecycleOwner) { result ->
            result.onSuccess { success ->
                if (success) {
                    Toast.makeText(context, "Commande annulée avec succès", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Échec de l'annulation de la commande", Toast.LENGTH_SHORT).show()
                }
            }
            result.onFailure { exception ->
                Toast.makeText(context, "Erreur: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("SingleOrderFragment", "Erreur lors de l'annulation: ${exception.message}", exception)
            }
        }


        // Observer la commande (1er call)
        singleOrderViewModel.orderDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess { order ->
                displayOrderDetails(view, order)

                val productIdList = order.orderItems.map { it.productId }
                productQuantities = order.orderItems.associate { it.productId to it.quantity }

                Log.d("SingleOrderFragment", "Product IDs envoyés au 2e call : $productIdList")
                Log.d("SingleOrderFragment", "voici le contenu de order: $order")

                singleOrderViewModel.fetchProductsByIds(productIdList)
            }

            result.onFailure { exception ->
                displayError(view, exception)
            }
        }


        // Observer les produits (2e call)
        singleOrderViewModel.productDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess { products ->
                // Log de la réponse du 2e call pour débogage
                Log.d("SingleOrderFragment", "Réponse du 2e call (produits récupérés) : $products")

                // Cacher la ProgressBar et afficher la RecyclerView
                loadingCard.visibility = View.GONE
                mainContent.visibility = View.VISIBLE

                // Mappage des produits pour l'adaptateur
                val productListForAdapter = products.map { product ->
                    OrderProduct(
                        id = product.id,
                        name = product.name,
                        description = product.description,
                        price = product.price,
                        quantity = productQuantities[product.id] ?: 0,  // Ajout de la quantité associée
                        category = product.category,
                        imageResId = R.drawable.ic_television // Utilise une ressource par défaut ou une dynamique
                    )
                }

                // Initialisation et mise à jour de l'adaptateur de la RecyclerView
                val adapter = OrderProductsAdapter(productListForAdapter)
                recyclerView.adapter = adapter
            }

            result.onFailure { exception ->
                // Log de l'erreur si le 2e call échoue
                Log.e("SingleOrderFragment", "Erreur lors du 2e call (fetchProductsByIds) : ${exception.message}", exception)

                // Cacher la ProgressBar en cas d'erreur
                loadingCard.visibility = View.GONE
                mainContent.visibility = View.VISIBLE // Optionnel : cacher la RecyclerView en cas d'erreur
            }
        }

        // Lancer le 1er call pour récupérer les infos commande
        if (orderId != -1) {
            singleOrderViewModel.getOrderDetails(orderId)
        }

        // Observer le statut de la commande et afficher le bouton suppression en conséquence
        singleOrderViewModel.orderDetails.observe(viewLifecycleOwner) { result ->
            result.onSuccess { order ->
                if (order.status == "cancelled") {
                    // Cacher ou désactiver le bouton "Annuler"
                    btnCancelOrder.visibility = View.GONE
                } else {
                    btnCancelOrder.visibility = View.VISIBLE
                }
            }
            result.onFailure {
                // Gérer l'erreur si besoin
            }
        }
    }

    private fun showCancelConfirmationDialog(orderId: Int) {
        context?.let {
            MaterialAlertDialogBuilder(it)
                .setTitle(R.string.cancel_confirmation_title)
                .setMessage(R.string.cancel_confirmation_message)
                .setPositiveButton(R.string.yes) { _, _ ->
                    // Appeler la méthode d'annulation
                    singleOrderViewModel.cancelOrder(orderId)
                }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }

    private fun displayOrderDetails(view: View, order: SingleOrderResponse) {
        view.findViewById<TextView>(R.id.orderId).text = "${order.id}"
        view.findViewById<TextView>(R.id.orderDate).text = "${order.orderDate} à ${order.orderTime}"
        view.findViewById<TextView>(R.id.orderStatus).text = "${order.status.capitalize()}"
        view.findViewById<TextView>(R.id.orderTotal).text = "${"%.2f".format(order.total)} €"
        val address = order.address
        view.findViewById<TextView>(R.id.orderAddress).text =
            "${address.street} ${address.number}, ${address.zipCode} ${address.city}, ${address.country}"
    }

    private fun displayError(view: View, exception: Throwable?) {
        view.findViewById<TextView>(R.id.orderStatus).text = "Erreur : ${exception?.message}"
    }
}
