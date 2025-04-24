import androidx.lifecycle.*
import com.example.clm_iot2.data.repository.StockRepository
import com.example.clm_iot2.model.Product  // Remplace avec ton import réel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val stockRepository = StockRepository()

    // Liste normale (sans LiveData) pour tester plus simplement
    private val cartProducts = mutableListOf<Product>()


    private val _availableStock = MutableLiveData<Map<Int, Int>>()
    val availableStock: LiveData<Map<Int, Int>> = _availableStock

    init {
        _availableStock.value = emptyMap()
    }

    // Retourne la liste des produits du panier
    fun getCartProducts(): List<Product> {
        println("Produits dans le panier : $cartProducts")
        return cartProducts.toList()
    }

    // Ajouter un produit au panier
    fun addProductToCart(produit: Product) {
        val existingProduct = cartProducts.find { it.id == produit.id }

        if (existingProduct != null) {
            existingProduct.quantity += produit.quantity
            println("✅ Produit déjà présent dans le panier : ${existingProduct.name}, Nouvelle quantité : ${existingProduct.quantity}")
        } else {
            // Récupère le stock avant d'ajouter au panier
            viewModelScope.launch(Dispatchers.IO) {
                val stock = stockRepository.getAvailableStock(produit.id)
                produit.availableStock = stock
                cartProducts.add(produit)
                println("🆕 Nouveau produit ajouté au panier : ${produit.name}, Stock dispo : $stock")
            }
        }
    }

    // Mettre à jour la quantité d'un produit
    fun updateProductQuantity(produit: Product, newQuantity: Int) {
        val productIndex = cartProducts.indexOfFirst { it.id == produit.id }
        if (productIndex != -1) {
            cartProducts[productIndex].quantity = newQuantity
            println("🔄 Quantité mise à jour : ${cartProducts[productIndex].name}, Nouvelle quantité : ${newQuantity}")
        }
    }

    // Supprimer un produit du panier
    fun removeProduct(produit: Product) {
        cartProducts.removeAll { it.id == produit.id }
        println("❌ Produit supprimé du panier : ${produit.name}")
    }

    // Fonction pour vider complètement le panier
    fun clearCart() {
        cartProducts.clear()
        println("🧹 Panier vidé avec succès.")
    }
}