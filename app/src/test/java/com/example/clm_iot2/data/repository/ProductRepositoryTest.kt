package com.example.clm_iot2.data.repository

import com.example.clm_iot2.model.OrderProduct
import com.example.clm_iot2.model.Category
import com.example.clm_iot2.model.Product
import RetrofitInstance
import ApiService
import ProductRepository
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import org.junit.Assert.*
import java.lang.reflect.Field

class ProductRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var productRepository: ProductRepository
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        val apiField: Field = RetrofitInstance::class.java.getDeclaredField("api")
        apiField.isAccessible = true
        apiField.set(RetrofitInstance, apiService)

        productRepository = ProductRepository()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getProducts should return a list of products when API call is successful`() = runBlocking {
        val mockProducts = listOf(
            Product(1, "Produit A", "Description A", 10.0, stock = 100),
            Product(2, "Produit B", "Description B", 20.0, stock = 200)
        )
    
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(mockProducts))
        mockWebServer.enqueue(mockResponse)
    
        val result = productRepository.getProducts()
    
        assertNotNull(result)
        assertEquals(2, result?.size)
        assertEquals("Produit A", result?.get(0)?.nom)
    }
    
    @Test
fun `getProductsByIds should return list of order products on success`() {
    val mockCategory = Category(id = 1, name = "Test Category", description = "Test Category")
    
    val mockOrderProducts = listOf(
        OrderProduct(
            id = 1,
            name = "Produit 1",
            price = 9.99,
            description = "Description 1",
            category = mockCategory,
            quantity = 2,
            imageResId = 0
        ),
        OrderProduct(
            id = 2,
            name = "Produit 2",
            price = 19.99,
            description = "Description 2",
            category = mockCategory,
            quantity = 1,
            imageResId = 0
        )
    )
    
    val mockResponse = MockResponse()
        .setResponseCode(200)
        .setBody(Gson().toJson(mockOrderProducts))
    mockWebServer.enqueue(mockResponse)
    
    var result: Result<List<OrderProduct>>? = null
    
    productRepository.getProductsByIds(listOf(1, 2)) {
        result = it
    }
    
    Thread.sleep(500)
    
    assertTrue(result!!.isSuccess)
    assertEquals(2, result!!.getOrNull()?.size)
}

    @Test
    fun `getProductsByCategory should return empty list when API fails`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val result = productRepository.getProductsByCategory("Electronics")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getProductsByIds should return failure on server error`() {
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        var result: Result<List<OrderProduct>>? = null

        productRepository.getProductsByIds(listOf(1)) {
            result = it
        }

        Thread.sleep(500)

        assertTrue(result!!.isFailure)
    }
}
