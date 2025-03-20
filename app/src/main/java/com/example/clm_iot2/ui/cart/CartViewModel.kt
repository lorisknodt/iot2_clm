import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.example.clm_iot2.model.Product  // Remplace avec ton import réel

class CartViewModel : ViewModel() {

    // Liste normale (sans LiveData) pour tester plus simplement
    private val cartProducts = mutableListOf<Product>()

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
            println("✅ Produit déjà présent dans le panier : ${existingProduct.nom}, Nouvelle quantité : ${existingProduct.quantity}")
        } else {
            cartProducts.add(produit)
            println("🆕 Nouveau produit ajouté au panier : ${produit.nom}, Quantité : ${produit.quantity}")
        }
    }

    // Mettre à jour la quantité d'un produit
    fun updateProductQuantity(produit: Product, newQuantity: Int) {
        val productIndex = cartProducts.indexOfFirst { it.id == produit.id }
        if (productIndex != -1) {
            cartProducts[productIndex].quantity = newQuantity
            println("🔄 Quantité mise à jour : ${cartProducts[productIndex].nom}, Nouvelle quantité : ${newQuantity}")
        }
    }

    // Supprimer un produit du panier
    fun removeProduct(produit: Product) {
        cartProducts.removeAll { it.id == produit.id }
        println("❌ Produit supprimé du panier : ${produit.nom}")
    }

    // Fonction pour vider complètement le panier
    fun clearCart() {
        cartProducts.clear()
        println("🧹 Panier vidé avec succès.")
    }
}