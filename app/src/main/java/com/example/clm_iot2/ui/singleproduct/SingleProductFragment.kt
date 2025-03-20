package com.example.clm_iot2.ui.singleproduct

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        // Exemple d'affichage fictif pour tester
        view.findViewById<TextView>(R.id.productName).text = "Produit Exemple"
        view.findViewById<TextView>(R.id.productDescription).text = "Description détaillée du produit"
        view.findViewById<TextView>(R.id.productPrice).text = "99.99 €"

        // Bouton retour
        view.findViewById<Button>(R.id.btnRetour).setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}
