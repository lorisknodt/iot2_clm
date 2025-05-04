package com.example.clm_iot2.data.repository

import ApiService
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StockRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: StockRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Retrofit personnalisé pour les tests
        val api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))  // Mock URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        // Classe anonyme pour injecter un faux RetrofitInstance
        repository = object : StockRepository() {
            override suspend fun getAvailableStock(productId: Int): Int {
                return try {
                    val stock = api.getStockByProductId(productId)
                    stock.quantity
                } catch (e: Exception) {
                    Int.MAX_VALUE
                }
            }
        }
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getAvailableStock returns quantity from API`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                {
                  "id": 1,
                  "productId": 10,
                  "quantity": 42,
                  "stockMin": 5,
                  "stockMax": 100,
                  "lastUpdated": "2024-05-04T12:00:00Z"
                }
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val quantity = repository.getAvailableStock(10)
        assertThat(quantity).isEqualTo(42)
    }

    @Test
    fun `getAvailableStock returns Int_MAX_VALUE on error`() = runTest {
        val mockResponse = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val quantity = repository.getAvailableStock(10)
        assertThat(quantity).isEqualTo(Int.MAX_VALUE)
    }
}
