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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Product
import com.example.clm_iot2.security.TokenManager
import com.example.clm_iot2.singleton.CurrentUser

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
        val productStock = arguments?.getInt("stock")
        val productImageUrl = arguments?.getString("imageUrl")

        // Références vers les vues
        val productNameTextView = view.findViewById<TextView>(R.id.productName)
        val productDescriptionTextView = view.findViewById<TextView>(R.id.productDescription)
        val productPriceTextView = view.findViewById<TextView>(R.id.productPrice)
        val productImageView = view.findViewById<ImageView>(R.id.productImage)

        // Affichage du texte
        productNameTextView.text = productName
        productDescriptionTextView.text = productDescription
        productPriceTextView.text = getString(R.string.product_detail_price, productPrice.toString())

        // Chargement de l'image avec Glide
        val baseUrl = "https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net"
        val fullImageUrl = baseUrl + (productImageUrl ?: "")

        val glideUrl = GlideUrl(
            fullImageUrl,
            LazyHeaders.Builder()
                .addHeader("Authorization", "Bearer ${TokenManager.getToken()}")
                .build()
        )

        Glide.with(this)
            .load(glideUrl)
            .error(R.drawable.ic_television)             // Image si erreur
            .into(productImageView)

        // Gestion du bouton ajouter
        view.findViewById<Button>(R.id.btnAddToCart).setOnClickListener {
            val product = Product(
                id = productId ?: 0,
                name = productName,
                description = productDescription,
                price = productPrice,
                stock = productStock ?: 0,
                imageUrl = productImageUrl
            )
            cartViewModel.addProductToCart(product)
            Toast.makeText(context, "Produit ajouté au panier", Toast.LENGTH_SHORT).show()
        }
    }


}
