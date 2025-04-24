package com.example.clm_iot2.ui.singleproduct

import CartViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product

class SingleProductFragment : Fragment() {

    // Viewmodel pour ajouter un produit
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_single_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Récupération des données du produit depuis le Bundle
        val productId = arguments?.getInt("id")
        val productName = arguments?.getString("name") ?: "Nom inconnu"
        val productDescription = arguments?.getString("description") ?: "Description indisponible"
        val productPrice = arguments?.getDouble("price") ?: 0.0
        val productImageResId = arguments?.getInt("imageResId")
        val productStock = arguments?.getInt("stock")

        // Affichage des données dans les vues
        view.findViewById<TextView>(R.id.productName).text = productName
        view.findViewById<TextView>(R.id.productDescription).text = productDescription
        view.findViewById<TextView>(R.id.productPrice).text = getString(R.string.product_detail_price, productPrice.toString())

        productImageResId?.let {
            view.findViewById<ImageView>(R.id.productImage).setImageResource(it)
        }

        // Gestion du bouton ajouter
        view.findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
            val product = Product(
                id = productId?: 0,
                name = productName,
                description = productDescription,
                price = productPrice,
                iconeResId = productImageResId ?: 0,
                stock = productStock?: 0
            )
            cartViewModel.addProductToCart(product) // Appel de ta fonction
            Toast.makeText(context, "Produit ajouté au panier", Toast.LENGTH_SHORT).show()

        }
    }

}
