package com.nityansh.dashboard.intent

import com.nityansh.domain.models.CategoryItem

sealed class CategoryIntent {
    object Nothing: CategoryIntent()
    data class AddCategory(val categoryName: String?, val isEnabled: Boolean?): CategoryIntent()
    data class UpdateCategory(val category: CategoryItem): CategoryIntent()
    data class DeleteCategory(val categoryId: Int): CategoryIntent()
}