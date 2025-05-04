package com.example.clm_iot2.data.repository

import RetrofitInstance
import ApiService
import com.example.clm_iot2.model.Category
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField

class CategoryRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var categoryRepository: CategoryRepository
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

        // Utiliser la réflexion Kotlin pour modifier la propriété api
        val property = RetrofitInstance::class.memberProperties.find { it.name == "api" }
        val javaField = property?.javaField
        requireNotNull(javaField) { "Property 'api' not found in RetrofitInstance" }
        javaField.isAccessible = true
        javaField.set(RetrofitInstance, apiService)

        categoryRepository = CategoryRepository()
    }

    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getCategories should return success with list of categories when API call is successful`() = runTest {
        val mockCategories = listOf(
            Category(1, "Electronics", "Electronic devices and gadgets"),
            Category(2, "Clothing", "Apparel and accessories")
        )
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(mockCategories))
        mockWebServer.enqueue(mockResponse)

        val result = categoryRepository.getCategories()

        assertTrue(result.isSuccess)
        val categories = result.getOrNull()
        assertEquals(2, categories?.size)
        assertEquals("Electronics", categories?.get(0)?.name)
        assertEquals("Clothing", categories?.get(1)?.name)
    }

    @Test
    fun `getCategories should return failure when API call fails`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("Server error")
        mockWebServer.enqueue(mockResponse)

        val result = categoryRepository.getCategories()

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() != null)
    }
}