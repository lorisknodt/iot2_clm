package com.example.clm_iot2.ui.category

import ProductRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clm_iot2.data.repository.CategoryRepository
import com.example.clm_iot2.model.Category
import com.example.clm_iot2.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val categoryRepository: CategoryRepository = CategoryRepository(),
    private val productRepository: ProductRepository = ProductRepository()
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products


    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchCategories() {
        viewModelScope.launch {
            val result = categoryRepository.getCategories()
            result
                .onSuccess { _categories.value = it }
                .onFailure { _error.value = it.message }
        }
    }

    // Fonction pour récupérer tous les produits (pour la catégorie "Tout")
    fun fetchProducts() {
        // Lancer une coroutine dans le ViewModel
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = productRepository.getProducts()
                _products.postValue(result ?: emptyList())  // Utiliser `.postValue()` pour MAJ depuis un thread en arrière-plan
            } catch (e: Exception) {
                _products.postValue(emptyList())
            }
        }
    }

    // Fonction pour récupérer les produits par catégorie
    fun fetchProductsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // Assurez-vous d'appeler l'API sur un thread arrière-plan
            val fetchedProducts = productRepository.getProductsByCategory(category)
            // Mettre à jour LiveData sur le thread principal
            _products.postValue(fetchedProducts)  // Utilise postValue() ici
        }
    }
}
