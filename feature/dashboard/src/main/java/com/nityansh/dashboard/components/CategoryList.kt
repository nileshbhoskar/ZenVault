package com.nityansh.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nityansh.dashboard.viewmodel.DashboardViewModel
import com.nityansh.domain.models.CategoryItem

@Composable
fun CategoryList(modifier: Modifier = Modifier, viewModel: DashboardViewModel, onViewTransactions: (CategoryItem) -> Unit) {
    LaunchedEffect(viewModel) {
        viewModel.loadTotalExpense()
    }

    val categories by viewModel.categoryUiState.categories.collectAsState()

    if (categories.isNotEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            content = {
                items(categories.size) { index ->
                    val categoryItem = categories[index]
                    Category(
                        modifier = Modifier.fillMaxWidth(),
                        categoryItem = categoryItem,
                        onViewTransactions = onViewTransactions
                    )
                }
            }
        )
    } else {
        Text(
            text = "No categories added yet. Click the \'Add Category button\' to add a new category.",
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}