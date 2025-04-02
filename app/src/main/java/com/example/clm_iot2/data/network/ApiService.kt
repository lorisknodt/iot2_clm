import com.example.clm_iot2.model.OrderRequest
import com.example.clm_iot2.model.OrderResponse
import com.example.clm_iot2.model.Product
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


// Interface qui contient les endpoints
interface ApiService {
    @GET("v1/products")
    suspend fun getProducts(): List<Product>

    @POST("/v1/order")
    fun createOrder(@Body orderRequest: OrderRequest): Call<OrderResponse>
}
