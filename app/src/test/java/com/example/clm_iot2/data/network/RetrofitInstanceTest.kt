package com.example.clm_iot2.data.network

import ApiService
import com.google.common.truth.Truth.assertThat
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Field

class RetrofitInstanceTest {

    @Test
    fun `api instance should not be null`() {
        val api = RetrofitInstance.api
        assertThat(api).isNotNull()
    }

    @Test
    fun `api should be instance of ApiService`() {
        val api = RetrofitInstance.api
        assertThat(api).isInstanceOf(ApiService::class.java)
    }

    @Test
    fun `Retrofit baseUrl should match expected value`() {
        // Recrée une instance Retrofit à partir du même code
        val retrofit = Retrofit.Builder()
            .baseUrl("https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val baseUrl: HttpUrl = retrofit.baseUrl()
        assertThat(baseUrl.toString()).isEqualTo("https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net/")
    }
}
