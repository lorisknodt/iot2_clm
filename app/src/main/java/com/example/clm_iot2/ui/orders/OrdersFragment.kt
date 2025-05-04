package com.example.clm_iot2.ui.orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.clm_iot2.R
import com.example.clm_iot2.adapter.OrderAdapter
import com.example.clm_iot2.model.Order

class OrdersFragment : Fragment() {

    private lateinit var ordersViewModel: OrdersViewModel
    private lateinit var orderAdapter: OrderAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingCard: androidx.cardview.widget.CardView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ordersViewModel = ViewModelProvider(this)[OrdersViewModel::class.java]

        val swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeRefreshLayout)

        val exploreButton = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnExploreProducts)
        exploreButton.setOnClickListener {
            // Quand le client n'as aucune commande effectuée
            findNavController().navigate(R.id.action_orderFragment_to_productsFragment)
        }


        // Passer l'adaptateur à la NavController
        orderAdapter = OrderAdapter(emptyList())

        val recyclerView = view.findViewById<RecyclerView>(R.id.orders_recycler_view)
        loadingCard = view.findViewById(R.id.loadingCard)


        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = orderAdapter

        ordersViewModel.orders.observe(viewLifecycleOwner) { orders ->
            // Cacher le chargement dans tous les cas
            loadingCard.visibility = View.GONE
            swipeRefreshLayout.isRefreshing = false

            if (orders.isEmpty()) {
                // Afficher l'état vide
                view.findViewById<LinearLayout>(R.id.emptyStateLayout).visibility = View.VISIBLE
                recyclerView.visibility = View.GONE

                Log.d("OrdersFragment", "Aucune commande trouvée.")
            } else {
                // Afficher la liste des commandes
                view.findViewById<LinearLayout>(R.id.emptyStateLayout).visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
                orderAdapter.updateData(orders)

                orders.forEach { order ->
                    Log.d("OrdersFragment", "Commande: ${order.id}, Statut: ${order.status}")
                }
            }
        }

        // Montrer la ProgressBar et masquer la RecyclerView avant de récupérer les données
        loadingCard.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE


        swipeRefreshLayout.setOnRefreshListener {
            ordersViewModel.fetchOrders()
        }
    }
}
