package com.example.clm_iot2.ui.products

import CartViewModel
import ProductAdapter
import ProductViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clm_iot2.R
import kotlinx.coroutines.launch

class ProductsFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()  // Ajout du ViewModel des produits

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewProduits)
        val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)

        recyclerView.layoutManager = LinearLayoutManager(context)

        // Observer pour les produits
        productViewModel.products.observe(viewLifecycleOwner, Observer { produits ->
            // Cacher la ProgressBar une fois les produits chargés
            progressBar.visibility = View.GONE

            // Vérifier si la liste des produits est vide
            if (produits.isNullOrEmpty()) {
                Toast.makeText(context, "Aucun produit trouvé", Toast.LENGTH_SHORT).show()
            } else {
                // Afficher les produits dans le RecyclerView
                recyclerView.adapter = ProductAdapter(produits) { product ->
                    cartViewModel.addProductToCart(product)
                    Toast.makeText(context, "${product.nom} ajouté au panier", Toast.LENGTH_SHORT).show()
                }
            }
        })

        // Affichage du loader pendant la récupération des données
        progressBar.visibility = View.VISIBLE

        // Lancer la récupération des produits
        productViewModel.fetchProducts()  // Appeler la fonction pour récupérer les produits
    }
}
