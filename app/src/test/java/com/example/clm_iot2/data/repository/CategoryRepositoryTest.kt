package com.example.clm_iot2.data.repository

import RetrofitInstance
import ApiService
import com.example.clm_iot2.model.Category
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import java.lang.reflect.Field

class CategoryRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var apiService: ApiService

    @Before
    fun setup() {
        // Initialisation du MockWebServer
        mockWebServer = MockWebServer()
        mockWebServer.start()

        // Configuration de Retrofit avec l'URL du MockWebServer
        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Création de l'instance de l'ApiService
        apiService = retrofit.create(ApiService::class.java)

        // Modification temporaire de RetrofitInstance.api pour utiliser le mock
        val apiField: Field = RetrofitInstance::class.java.getDeclaredField("api")
        apiField.isAccessible = true
        apiField.set(RetrofitInstance, apiService)

        // Initialisation du repository
        categoryRepository = CategoryRepository()
    }

    @After
    fun tearDown() {
        // Arrêt du MockWebServer après chaque test
        mockWebServer.shutdown()
    }

    @Test
    fun `getCategories should return success with list of categories when API call is successful`() = runBlocking {
        // Préparation de la réponse mockée
        val mockCategories = listOf(
            Category(1, "Electronics", "Electronic devices and gadgets"),
            Category(2, "Clothing", "Apparel and accessories")
        )
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(Gson().toJson(mockCategories))
        mockWebServer.enqueue(mockResponse)

        // Appel de la méthode à tester
        val result = categoryRepository.getCategories()

        // Vérifications
        assertTrue(result.isSuccess)
        val categories = result.getOrNull()
        assertEquals(2, categories?.size)
        assertEquals("Electronics", categories?.get(0)?.name)
        assertEquals("Clothing", categories?.get(1)?.name)
    }

    @Test
    fun `getCategories should return failure when API call fails`() = runBlocking {
        // Préparation d'une réponse d'erreur
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("Server error")
        mockWebServer.enqueue(mockResponse)

        // Appel de la méthode à tester
        val result = categoryRepository.getCategories()

        // Vérifications
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() != null)
    }
}