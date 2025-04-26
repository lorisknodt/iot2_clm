import com.example.clm_iot2.model.OrderProduct
import com.example.clm_iot2.model.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductRepository {
    val apiService = RetrofitInstance.getApi()

    // Fonction suspendue pour récupérer les produits
    suspend fun getProducts(): List<Product>? {
        return try {
            // Appel de l'API avec Retrofit (fonction suspendue)
            apiService.getProducts()
        } catch (e: Exception) {
            // En cas d'erreur, retourne null
            null
        }
    }

    suspend fun getProductsByCategory(category: String): List<Product> {
        return try {
            apiService.getProductsByCategory(category)
        } catch (e: Exception) {
            // En cas d'erreur, retourne une liste vide
            emptyList() // Fonction Kotlin qui retourne une liste vide
        }
    }

    fun getProductsByIds(productIds: List<Int>, callback: (Result<List<OrderProduct>>) -> Unit) {
        // Appel asynchrone avec Retrofit
        val call = apiService.getProductsByIDs(productIds)

        call.enqueue(object : Callback<List<OrderProduct>> {
            override fun onResponse(
                call: Call<List<OrderProduct>>,
                response: Response<List<OrderProduct>>
            ) {
                if (response.isSuccessful) {
                    // Si la réponse est réussie, on envoie les produits au callback
                    val products = response.body()
                    callback(Result.success(products ?: emptyList()))
                } else {
                    // En cas d'échec de la réponse, on renvoie une erreur
                    callback(Result.failure(Exception("Erreur API: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<List<OrderProduct>>, t: Throwable) {
                // En cas d'échec de l'appel (erreur réseau, etc.), on envoie une erreur
                callback(Result.failure(t))
            }
        })
    }

}
