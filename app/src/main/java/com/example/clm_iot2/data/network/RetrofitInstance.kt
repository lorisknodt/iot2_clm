import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://apiserver-bsdfh4gmduh8bsep.francecentral-01.azurewebsites.net/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Permet de parser automatiquement le JSON
            .build()
            .create(ApiService::class.java)
    }
}
