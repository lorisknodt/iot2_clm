import com.example.clm_iot2.model.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


// Interface qui contient les endpoints
interface ApiService {

    // PRODUCT CALLS
    @GET("v1/products")
    suspend fun getProducts(): List<Product>

    @GET("/v1/products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<Product>

    @POST("/v1/products/ids")
    fun getProductsByIDs(@Body productIds: List<Int>): Call<List<OrderProduct>>

    // ORDER CALLS
    @POST("/v1/order")
    fun createOrder(@Body orderRequest: OrderRequest): Call<OrderResponse>

    @GET("/v1/orders")
    suspend fun getOrders(): List<Order>


    @GET("/v1/order/{orderId}")
    suspend fun getOrderById(@Path("orderId") orderId: Int): SingleOrderResponse


    // CATEGORY CALLS
    @GET("/v1/categories")
    suspend fun getCategories(): List<Category>

}
