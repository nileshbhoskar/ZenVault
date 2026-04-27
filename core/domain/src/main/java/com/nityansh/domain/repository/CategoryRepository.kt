package com.nityansh.domain.repository

import com.nityansh.domain.models.CategoryItem
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    fun getAllCategories(): Flow<List<CategoryItem>>

    suspend fun addCategory(category: CategoryItem)

    suspend fun updateCategory(category: CategoryItem)

    suspend fun getCategoryWiseTotalAmount(): Flow<Map<CategoryItem, Double>>
}