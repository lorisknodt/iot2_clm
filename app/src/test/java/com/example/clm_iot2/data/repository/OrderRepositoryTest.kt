package com.example.clm_iot2.repository

import RetrofitInstance
import ApiService
import ProductRepository
import com.example.clm_iot2.model.*
import com.google.gson.Gson
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.CompletableDeferred
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Assertions.assertEquals
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Field

class OrderRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var orderRepository: OrderRepository
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

        orderRepository = OrderRepository()
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `createOrder should return success when API responds with 200`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(OrderResponse(1L, "SUCCESS", "Commande créée avec succès")))
        mockWebServer.enqueue(mockResponse)

        val orderRequest = OrderRequest(
            orderItemsCreateDTOList = listOf(OrderItem(1, 2)),
            adresseId = 1,
            newAddress = NewAddress("Rue A", "Ville", "10", "12345", "France")
        )

        val deferredResult = CompletableDeferred<Pair<Boolean, String?>>()
        orderRepository.createOrder(orderRequest) { success, message ->
            deferredResult.complete(success to message)
        }

        val (resultSuccess, resultMessage) = deferredResult.await()

        assertTrue(resultSuccess)
        assertEquals("Commande créée avec succès", resultMessage)
    }

    @Test
    fun `createOrder should return failure when API responds with error`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(400)
        mockWebServer.enqueue(mockResponse)

        val orderRequest = OrderRequest(
            orderItemsCreateDTOList = listOf(OrderItem(1, 2)),
            adresseId = 1,
            newAddress = NewAddress("Rue B", "Ville", "20", "54321", "France")
        )

        val deferredResult = CompletableDeferred<Pair<Boolean, String?>>()
        orderRepository.createOrder(orderRequest) { success, message ->
            deferredResult.complete(success to message)
        }

        val (resultSuccess, resultMessage) = deferredResult.await()

        assertTrue(!resultSuccess)
        assertEquals("Erreur lors de la création de la commande", resultMessage)
    }

    @Test
    fun `getOrders should return success with list of orders`() = runTest {
        val mockOrders = listOf(
            Order(1, "2023-01-01", "10:00", "EN_COURS", "BanqueA", 100.0,
                Address(1, "Rue 1", "Ville", "10", "75000", "France"), 101),
            Order(2, "2023-01-02", "11:00", "LIVREE", "BanqueB", 200.0,
                Address(2, "Rue 2", "Ville", "20", "69000", "France"), 102)
        )
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(mockOrders))
        mockWebServer.enqueue(mockResponse)

        val result = orderRepository.getOrders()

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrNull()?.size)
    }

    @Test
    fun `getOrders should return failure when API fails`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val result = orderRepository.getOrders()

        assertTrue(result.isFailure)
    }

    @Test
    fun `getOrderDetails should return success with order details`() = runTest {
        val mockOrder = SingleOrderResponse(
            id = 1,
            orderDate = "2023-01-01",
            orderTime = "10:00",
            status = "EN_COURS",
            bankName = "BanqueA",
            total = 150.0,
            orderItems = listOf(OrderItem(1, 2)),
            address = Address(1, "Rue Z", "Ville", "10", "75000", "France"),
            clientId = 123
        )
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(mockOrder))
        mockWebServer.enqueue(mockResponse)

        val result = orderRepository.getOrderDetails(1)

        assertTrue(result.isSuccess)
        assertEquals("EN_COURS", result.getOrNull()?.status)
    }

    @Test
    fun `getOrderDetails should return failure when API fails`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val result = orderRepository.getOrderDetails(999)

        assertTrue(result.isFailure)
    }
}