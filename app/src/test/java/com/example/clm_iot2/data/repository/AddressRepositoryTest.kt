package com.example.clm_iot2.data.repository

import ApiService
import AddressRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddressRepositoryTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var repository: AddressRepository

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        repository = AddressRepository(api)
    }

    @After
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getAddresses returns success when API responds with valid data`() = runTest {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(
                """
                [
                    {
                        "id": 1,
                        "street": "123 rue Exemple",
                        "city": "Paris",
                        "number": "5A",
                        "zipCode": "75000",
                        "country": "France"
                    }
                ]
                """.trimIndent()
            )
        mockWebServer.enqueue(mockResponse)

        val result = repository.getAddresses(10)

        assertThat(result.isSuccess).isTrue()
        assertThat(result.getOrNull()).hasSize(1)
        assertThat(result.getOrNull()!![0].city).isEqualTo("Paris")
    }

    @Test
    fun `getAddresses returns failure when API returns error`() = runTest {
        val mockResponse = MockResponse().setResponseCode(500)
        mockWebServer.enqueue(mockResponse)

        val result = repository.getAddresses(10)

        assertThat(result.isFailure).isTrue()
    }
}
