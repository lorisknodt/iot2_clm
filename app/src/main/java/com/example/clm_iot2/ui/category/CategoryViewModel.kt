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

    private val _products = MutableLiveData<List<Product>?>()
    val products: MutableLiveData<List<Product>?> get() = _products

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Nouvel état pour gérer les erreurs produits spécifiquement
    private val _productLoadError = MutableLiveData<Boolean>(false)
    val productLoadError: LiveData<Boolean> get() = _productLoadError

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
        // Indiquer que le chargement commence
        _isLoading.postValue(true)
        // Réinitialiser l'état d'erreur
        _productLoadError.postValue(false)

        // Lancer une coroutine dans le ViewModel
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = productRepository.getProducts()
                if (result != null && result.isNotEmpty()) {
                    _products.postValue(result)
                    _productLoadError.postValue(false)
                } else {
                    _productLoadError.postValue(true)
                    _products.postValue(emptyList())
                }
            } catch (e: Exception) {
                _productLoadError.postValue(true)
                _products.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    // Fonction pour récupérer les produits par catégorie
    fun fetchProductsByCategory(category: String) {
        // Indiquer que le chargement commence
        _isLoading.postValue(true)
        // Réinitialiser l'état d'erreur
        _productLoadError.postValue(false)

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fetchedProducts = productRepository.getProductsByCategory(category)
                if (fetchedProducts != null && fetchedProducts.isNotEmpty()) {
                    _products.postValue(fetchedProducts)
                    _productLoadError.postValue(false)
                } else {
                    _productLoadError.postValue(true)
                    _products.postValue(emptyList())
                }
            } catch (e: Exception) {
                _productLoadError.postValue(true)
                _products.postValue(emptyList())
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}