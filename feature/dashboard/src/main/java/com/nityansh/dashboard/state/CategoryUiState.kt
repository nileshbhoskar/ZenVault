package com.nityansh.dashboard.state

import com.nityansh.domain.models.CategoryItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class CategoryUiState(
    val isLoading: Boolean = false,
    val categories: StateFlow<List<CategoryItem>> = MutableStateFlow(emptyList()),
    val isAddDialogVisible: Boolean = false,
    val totalAmount: String = "",
    val error: String? = null,
)
