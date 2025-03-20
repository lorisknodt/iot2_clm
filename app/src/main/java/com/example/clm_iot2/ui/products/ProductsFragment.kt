package com.example.clm_iot2.ui.products

import CartViewModel
import ProductAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product

class ProductsFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()

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
            Product(1, "Téléphone","Samsung galaxy S29 24gb RAM telephone de goat", 299.99, R.drawable.ic_television, 1, 1),
            Product(2, "Télévision", "Samsung galaxy television superflat 2.3mm thick GOD DAYUM",499.99, R.drawable.ic_television, 1, 1)
            // Ajouter d'autres produits ici
        )

        // Associe l'adaptateur au RecyclerView
        recyclerView.adapter = ProductAdapter(produits) { product ->
            // Logique d'ajout au panier, par exemple :
            cartViewModel.addProductToCart(product)
            cartViewModel.getCartProducts()  // Affiche les produits dans la console pour vérifier
            Toast.makeText(context, "${product.nom} ajouté au panier", Toast.LENGTH_SHORT).show()
        }
    }

}