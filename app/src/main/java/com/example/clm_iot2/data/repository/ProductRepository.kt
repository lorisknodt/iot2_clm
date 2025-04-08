import com.example.clm_iot2.model.Product

class ProductRepository {

    // Fonction suspendue pour récupérer les produits
    suspend fun getProducts(): List<Product>? {
        return try {
            // Appel de l'API avec Retrofit (fonction suspendue)
            RetrofitInstance.api.getProducts()
        } catch (e: Exception) {
            // En cas d'erreur, retourne null
            null
        }
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        return try {
            RetrofitInstance.api.getProductsByCategory(category)
        } catch (e: Exception) {
            // En cas d'erreur, retourne une liste vide
            emptyList() // Fonction Kotlin qui retourne une liste vide
        }
    }
}
