package com.example.clm_iot2.ui.singleproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.clm_iot2.R

class SingleProductFragment : Fragment() {

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
        val productName = arguments?.getString("name") ?: "Nom inconnu"
        val productDescription = arguments?.getString("description") ?: "Description indisponible"
        val productPrice = arguments?.getDouble("price") ?: 0.0
        val productImageResId = arguments?.getInt("imageResId")

        // Affichage des données dans les vues
        view.findViewById<TextView>(R.id.productName).text = productName
        view.findViewById<TextView>(R.id.productDescription).text = productDescription
        view.findViewById<TextView>(R.id.productPrice).text = "Prix : ${productPrice} €"

        productImageResId?.let {
            view.findViewById<ImageView>(R.id.productImage).setImageResource(it)
        }

        // Gestion du bouton retour
        view.findViewById<Button>(R.id.btnRetour).setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}
