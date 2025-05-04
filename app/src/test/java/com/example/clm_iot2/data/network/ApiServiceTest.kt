package com.example.clm_iot2.data.network

import ApiService
import com.example.clm_iot2.model.*
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ApiService

    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getProducts returns list of products`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                [
                    {
                        "id": 1,
                        "name": "Produit A",
                        "description": "Desc",
                        "price": 10.0,
                        "imageUrl": "url",
                        "category": "Boissons"
                    }
                ]
            """.trimIndent())

        mockWebServer.enqueue(mockResponse)

        val result = apiService.getProducts()

        assertThat(result).isNotEmpty()
        assertThat(result[0].nom).isEqualTo("Produit A")
    }

    @Test
    fun `getStockByProductId returns stock response`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody("""
                {
                    "id": 1,
                    "productId": 5,
                    "quantity": 12,
                    "stockMin": 2,
                    "stockMax": 30,
                    "lastUpdated": "2024-12-01T10:00:00"
                }
            """.trimIndent())

        mockWebServer.enqueue(mockResponse)

        val result = apiService.getStockByProductId(5)

        assertThat(result.productId).isEqualTo(5)
        assertThat(result.quantity).isEqualTo(12)
    }
}