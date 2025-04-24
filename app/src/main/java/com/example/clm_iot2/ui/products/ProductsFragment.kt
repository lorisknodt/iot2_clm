package com.example.clm_iot2.ui.products

import CartViewModel
import CategoryAdapter
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.clm_iot2.R
import com.example.clm_iot2.model.Category
import com.example.clm_iot2.ui.category.CategoryViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import androidx.constraintlayout.widget.ConstraintLayout

class ProductsFragment : Fragment() {

    private val cartViewModel: CartViewModel by activityViewModels()
    private val productViewModel: ProductViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by activityViewModels()

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var errorLayout: ConstraintLayout
    private lateinit var contentLayout: ConstraintLayout
    private lateinit var retryButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialiser les vues
        initViews(view)
        // Configurer les listeners
        setupListeners()
        // Configurer les observateurs
        setupObservers()
        // Récupérer les données initiales
        loadInitialData()
    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.recyclerViewProduits)
        progressBar = view.findViewById(R.id.progressBar)
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout)
        errorLayout = view.findViewById(R.id.errorLayout)
        contentLayout = view.findViewById(R.id.contentLayout)
        retryButton = view.findViewById(R.id.retryButton)

        recyclerView.layoutManager = LinearLayoutManager(context)

        val recyclerViewCategories = view.findViewById<RecyclerView>(R.id.recyclerViewCategories)
        recyclerViewCategories.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Observer pour les catégories
        lifecycleScope.launch {
            categoryViewModel.categories.collect { categories ->
                val allCategory = Category(id = -1, name = "Tout", description = "Tous les produits")
                val allCategories = listOf(allCategory) + categories

                recyclerViewCategories.adapter = CategoryAdapter(allCategories) { selectedCategory ->
                    if (selectedCategory.name == "Tout") {
                        categoryViewModel.fetchProducts()
                    } else {
                        categoryViewModel.fetchProductsByCategory(selectedCategory.name)
                    }
                }
            }
        }
    }

    private fun setupListeners() {
        swipeRefreshLayout.setOnRefreshListener {
            categoryViewModel.fetchProducts()
        }

        retryButton.setOnClickListener {
            errorLayout.visibility = View.GONE
            contentLayout.visibility = View.VISIBLE
            categoryViewModel.fetchProducts()
        }
    }

    private fun setupObservers() {
        // Observer pour l'état de chargement
        categoryViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observer pour les erreurs de chargement des produits
        categoryViewModel.productLoadError.observe(viewLifecycleOwner) { hasError ->
            if (hasError) {
                contentLayout.visibility = View.GONE
                errorLayout.visibility = View.VISIBLE
                swipeRefreshLayout.isRefreshing = false
            } else {
                contentLayout.visibility = View.VISIBLE
                errorLayout.visibility = View.GONE
            }
        }

        // Observer pour les produits
        categoryViewModel.products.observe(viewLifecycleOwner) { produits ->
            // Terminer le rafraîchissement si actif
            swipeRefreshLayout.isRefreshing = false

            if (!produits.isNullOrEmpty()) {
                // Afficher les produits dans le RecyclerView
                recyclerView.adapter = ProductAdapter(produits) { product ->
                    cartViewModel.addProductToCart(product)
                    Toast.makeText(context,
                        getString(R.string.toast_product_added_to_cart, product.name), Toast.LENGTH_SHORT).show()
                }
            } else {
                // Si productLoadError n'est pas déjà activé, montrer le toast
                if (categoryViewModel.productLoadError.value != true) {
                    Toast.makeText(context, getString(R.string.toast_no_products_found), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadInitialData() {
        // Récupérer les catégories
        categoryViewModel.fetchCategories()
        // Lancer la récupération des produits (par défaut : tous les produits)
        categoryViewModel.fetchProducts()
    }
}