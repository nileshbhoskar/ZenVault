package com.nityansh.dashboard.state

import com.nityansh.domain.models.CategoryItem

data class CategoryUiState(
    val isLoading: Boolean = false,
    val categories: List<CategoryItem> = emptyList(),
    val error: String? = null,
)
