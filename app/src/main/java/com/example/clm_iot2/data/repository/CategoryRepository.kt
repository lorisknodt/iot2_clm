package com.example.clm_iot2.data.repository

import com.example.clm_iot2.model.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoryRepository {
    // Fonction suspendue pour récupérer les categories
    suspend fun getCategories(): Result<List<Category>> {
        return withContext(Dispatchers.IO) {
            try {
                val categories = RetrofitInstance.api.getCategories()
                Result.success(categories)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}