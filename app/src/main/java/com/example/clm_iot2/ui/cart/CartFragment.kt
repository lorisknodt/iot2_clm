package com.example.clm_iot2.ui.cart

import CartAdapter
import CartViewModel
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var cartAdapter: CartAdapter

    // Utilisation correcte de activityViewModels pour conserver les données
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart)

        // Initialisation de l'adaptateur avec une liste vide
        cartAdapter = CartAdapter(
            mutableListOf(),
            onQuantityChange = { produit, newQuantity ->
                cartViewModel.updateProductQuantity(produit, newQuantity)
            },
            onRemoveProduct = { produit ->
                cartViewModel.removeProduct(produit)
            }
        )

        recyclerViewCart.adapter = cartAdapter
        recyclerViewCart.layoutManager = LinearLayoutManager(context)

        // Mise à jour de la liste de produits du panier
        val produits = cartViewModel.getCartProducts()
        println("Affichage du panier dans le fragment : $produits")

        cartAdapter.updateData(produits)
    }
}
