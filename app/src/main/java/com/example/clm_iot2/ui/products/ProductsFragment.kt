package com.example.clm_iot2.ui.products

import ProductAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product

class ProductsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupère le RecyclerView depuis le layout
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProduits)

        // Définit le LayoutManager (ici, LinearLayoutManager pour une liste verticale)
        recyclerView.layoutManager = LinearLayoutManager(context)

        // Crée une liste de produits (pour l'instant, définis en dur)
        val produits = listOf(
            Product("Téléphone", 299.99, R.drawable.ic_television),
            Product("Télévision", 499.99, R.drawable.ic_television)
            // Tu peux ajouter d'autres produits ici
        )

        // Associe l'adaptateur au RecyclerView
        recyclerView.adapter = ProductAdapter(produits)
    }

}