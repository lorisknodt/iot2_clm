package com.example.clm_iot2.repository

import RetrofitInstance
import ApiService
import com.example.clm_iot2.model.*
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Field
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals

class OrderRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var orderRepository: OrderRepository
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

        orderRepository = OrderRepository()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `createOrder should return success when API responds with 200`() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(OrderResponse(1L, "SUCCESS", "Commande créée avec succès")))
        mockWebServer.enqueue(mockResponse)

        val orderRequest = OrderRequest(
            orderItemsCreateDTOList = listOf(OrderItem(1, 2)),
            adresseId = 1,
            newAddress = NewAddress("Rue A", "Ville", "10", "12345", "France")
        )

        var resultSuccess: Boolean? = null
        var resultMessage: String? = null

        orderRepository.createOrder(orderRequest) { success, message ->
            resultSuccess = success
            resultMessage = message
        }

        Thread.sleep(200) // attendre le callback

        assertTrue(resultSuccess == true)
        assertEquals("Commande créée avec succès", resultMessage)
    }

    @Test
    fun `createOrder should return failure when API responds with error`() {
        val mockResponse = MockResponse()
            .setResponseCode(400)
        mockWebServer.enqueue(mockResponse)

        val orderRequest = OrderRequest(
            orderItemsCreateDTOList = listOf(OrderItem(1, 2)),
            adresseId = 1,
            newAddress = NewAddress("Rue B", "Ville", "20", "54321", "France")
        )

        var resultSuccess: Boolean? = null
        var resultMessage: String? = null

        orderRepository.createOrder(orderRequest) { success, message ->
            resultSuccess = success
            resultMessage = message
        }

        Thread.sleep(200)

        assertTrue(resultSuccess == false)
        assertEquals("Erreur lors de la création de la commande", resultMessage)
    }

    @Test
    fun `getOrders should return success with list of orders`() = runBlocking {
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
    fun `getOrders should return failure when API fails`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val result = orderRepository.getOrders()

        assertTrue(result.isFailure)
    }

    @Test
    fun `getOrderDetails should return success with order details`() = runBlocking {
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
    fun `getOrderDetails should return failure when API fails`() = runBlocking {
        val mockResponse = MockResponse()
            .setResponseCode(404)
        mockWebServer.enqueue(mockResponse)

        val result = orderRepository.getOrderDetails(999)

        assertTrue(result.isFailure)
    }
}
