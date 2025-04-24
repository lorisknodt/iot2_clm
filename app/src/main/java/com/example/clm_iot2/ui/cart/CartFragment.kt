package com.example.clm_iot2.ui.cart

import CartAdapter
import CartViewModel
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R

class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var recyclerViewCart: RecyclerView
    private lateinit var cartAdapter: CartAdapter

    // Utilisation de activityViewModels pour conserver les données
    private val cartViewModel: CartViewModel by activityViewModels()

    private fun calculerTotal() {
        val total = cartViewModel.getCartProducts().sumOf { it.price * it.quantity }
        view?.findViewById<TextView>(R.id.totalPrix)?.text = "Total : ${"%.2f".format(total)} €"
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewCart = view.findViewById(R.id.recyclerViewCart)
        val emptyCartLayout = view.findViewById<LinearLayout>(R.id.emptyCartLayout)
        val btnPasserCommande = view.findViewById<Button>(R.id.btnPasserCommande)

        val produits = cartViewModel.getCartProducts()

        cartAdapter = CartAdapter(
            produits.toMutableList(),
            onQuantityChange = { produit, newQuantity ->
                cartViewModel.updateProductQuantity(produit, newQuantity)
                updateCartVisibility(cartAdapter.itemCount, emptyCartLayout, btnPasserCommande)
                calculerTotal() },
            onRemoveProduct = { produit ->
                cartViewModel.removeProduct(produit)

                val updatedProducts = cartViewModel.getCartProducts()
                cartAdapter.updateData(updatedProducts)

                updateCartVisibility(updatedProducts.size, emptyCartLayout, btnPasserCommande)
                calculerTotal()
            },
        )

        recyclerViewCart.adapter = cartAdapter
        recyclerViewCart.layoutManager = LinearLayoutManager(context)

        cartAdapter.updateData(produits)
        updateCartVisibility(cartAdapter.itemCount, emptyCartLayout, btnPasserCommande)

        calculerTotal()

        btnPasserCommande.setOnClickListener {
            if (cartAdapter.itemCount == 0) {
                Toast.makeText(requireContext(), getString(R.string.toast_empty_cart), Toast.LENGTH_SHORT).show()
            } else {
                requireView().findNavController().navigate(R.id.action_cartFragment_to_orderConfirmationFragment)
            }
        }
    }

    private fun updateCartVisibility(
        itemCount: Int,
        emptyCartLayout: LinearLayout,
        btnPasserCommande: Button
    ) {
        if (itemCount == 0) {
            emptyCartLayout.visibility = View.VISIBLE
            recyclerViewCart.visibility = View.GONE
            btnPasserCommande.isEnabled = false
            btnPasserCommande.alpha = 0.5f
        } else {
            emptyCartLayout.visibility = View.GONE
            recyclerViewCart.visibility = View.VISIBLE
            btnPasserCommande.isEnabled = true
            btnPasserCommande.alpha = 1f
        }
    }

}
