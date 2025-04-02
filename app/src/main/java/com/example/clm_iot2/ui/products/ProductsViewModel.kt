import ProductRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clm_iot2.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val repository = ProductRepository()

    // LiveData pour exposer la liste des produits
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> get() = _products

    // Fonction pour récupérer les produits depuis le Repository
    fun fetchProducts() {
        // Lancer une coroutine dans le ViewModel
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = repository.getProducts()
                _products.postValue(result ?: emptyList())  // Utiliser `.postValue()` pour MAJ depuis un thread en arrière-plan
            } catch (e: Exception) {
                _products.postValue(emptyList())
            }
        }
    }
}
