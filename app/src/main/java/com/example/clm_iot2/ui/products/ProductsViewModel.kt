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

}
