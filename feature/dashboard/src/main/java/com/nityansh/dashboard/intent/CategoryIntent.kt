package com.nityansh.dashboard.intent

import com.nityansh.domain.models.CategoryItem

sealed class CategoryIntent {

    object Inactive: CategoryIntent()
    object Loading: CategoryIntent()
    data class Success(
        val categories: List<CategoryItem>
    ): CategoryIntent()
    data class Error(
        val message: String
    ): CategoryIntent()

}