package com.example.clm_iot2.data.repository

import ApiService
import RetrofitInstance
import ProductRepository
import com.example.clm_iot2.model.OrderProduct
import com.example.clm_iot2.model.Category
import com.example.clm_iot2.model.Product
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.CompletableDeferred
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import java.lang.reflect.Field

class ProductRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var productRepository: ProductRepository
    private lateinit var apiService: ApiService

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Modifier le champ api$delegate dans RetrofitInstance
        val delegateField: Field = RetrofitInstance::class.java.getDeclaredField("api\$delegate")
        delegateField.isAccessible = true
        val lazyDelegate = delegateField.get(RetrofitInstance) as Lazy<*>
        // Remplacer la valeur du Lazy par notre apiService
        val valueField: Field = lazyDelegate::class.java.getDeclaredField("value")
        valueField.isAccessible = true
        valueField.set(lazyDelegate, apiService)

        productRepository = ProductRepository()
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getProducts should return a list of products when API call is successful`() = runTest {
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
    fun `getProductsByIds should return list of order products on success`() = runTest {
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

        val deferredResult = CompletableDeferred<Result<List<OrderProduct>>>()
        productRepository.getProductsByIds(listOf(1, 2)) {
            deferredResult.complete(it)
        }

        val result = deferredResult.await()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getProductsByCategory should return empty list when API fails`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val result = productRepository.getProductsByCategory("Electronics")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getProductsByIds should return failure on server error`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val deferredResult = CompletableDeferred<Result<List<OrderProduct>>>()
        productRepository.getProductsByIds(listOf(1)) {
            deferredResult.complete(it)
        }

        val result = deferredResult.await()

        assertTrue(result.isFailure)
    }
}