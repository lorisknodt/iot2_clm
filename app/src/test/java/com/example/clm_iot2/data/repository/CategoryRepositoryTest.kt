package com.example.clm_iot2.data.repository

import com.example.clm_iot2.model.Category
import com.example.clm_iot2.data.network.ApiService
import com.example.clm_iot2.data.network.RetrofitInstance
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.clm_iot2.data.remote.ApiService
import com.google.gson.Gson
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue

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

        // Initialisation du repository avec l'ApiService mocké
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

        // Création d'une instance temporaire de RetrofitInstance pour le test
        val originalApi = RetrofitInstance.api
        val retrofitField = RetrofitInstance::class.java.getDeclaredField("api")
        retrofitField.isAccessible = true
        retrofitField.set(RetrofitInstance, apiService)

        try {
            // Appel de la méthode à tester
            val result = categoryRepository.getCategories()

            // Vérifications
            assertTrue(result.isSuccess)
            val categories = result.getOrNull()
            assertEquals(2, categories?.size)
            assertEquals("Electronics", categories?.get(0)?.name)
            assertEquals("Clothing", categories?.get(1)?.name)
        } finally {
            // Restauration de l'ApiService original
            retrofitField.set(RetrofitInstance, originalApi)
        }
    }

    @Test
    fun `getCategories should return failure when API call fails`() = runBlocking {
        // Préparation d'une réponse d'erreur
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("Server error")
        mockWebServer.enqueue(mockResponse)

        // Création d'une instance temporaire de RetrofitInstance pour le test
        val originalApi = RetrofitInstance.api
        val retrofitField = RetrofitInstance::class.java.getDeclaredField("api")
        retrofitField.isAccessible = true
        retrofitField.set(RetrofitInstance, apiService)

        try {
            // Appel de la méthode à tester
            val result = categoryRepository.getCategories()

            // Vérifications
            assertTrue(result.isFailure)
            assertTrue(result.exceptionOrNull() != null)
        } finally {
            // Restauration de l'ApiService original
            retrofitField.set(RetrofitInstance, originalApi)
        }
    }
}