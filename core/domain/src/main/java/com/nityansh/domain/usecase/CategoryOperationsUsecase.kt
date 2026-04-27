package com.nityansh.domain.usecase

import com.nityansh.domain.models.CategoryItem
import com.nityansh.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryOperationsUsecase @Inject constructor(val repository: CategoryRepository) {

     fun getAllCategories() = repository.getAllCategories()

     suspend fun addCategory(category: CategoryItem) = repository.addCategory(category)

     suspend fun updateCategory(category: CategoryItem) = repository.updateCategory(category)

     suspend fun getCategoryWiseTotalAmount() = repository.getCategoryWiseTotalAmount()
}