package com.nityansh.dashboard

import com.nityansh.domain.models.CategoryItem
import com.nityansh.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class FakeCategoryRepository: CategoryRepository {

    val categories = MutableStateFlow<List<CategoryItem>>(emptyList())

    init {
        val list = mutableListOf(
            CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 500.0),
            CategoryItem(id = 2, name = "Transport", enabled = true, totalAmount = 2500.0)
        )
        categories.value = list
    }
    override fun getAllCategories(): Flow<List<CategoryItem>> {
        return categories
    }

    override suspend fun addCategory(category: CategoryItem): Long {
        val currentList = categories.value.toMutableList()
        categories.value = currentList.add(category).let { currentList }
        return category.id.toLong()
    }

    override suspend fun updateCategory(category: CategoryItem): Long {
        val currentList = categories.value.toMutableList()
        categories.value = currentList.add(category).let { currentList }
        return category.id.toLong()
    }

    override suspend fun getCategoryWiseTotalAmount(): Flow<Map<CategoryItem, Double>> {
        return emptyFlow()
    }
}