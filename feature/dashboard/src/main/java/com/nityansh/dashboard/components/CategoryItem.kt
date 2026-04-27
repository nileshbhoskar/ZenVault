package com.nityansh.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nityansh.domain.models.CategoryItem

@Composable
fun Category(
    modifier: Modifier = Modifier,
    categoryItem: CategoryItem,
    onViewTransactions: (CategoryItem) -> Unit
) {
    Column(
        modifier = modifier
            .border(1.dp, Color.Black, shape = RoundedCornerShape(8.dp))
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .clickable(enabled = true, onClick = { onViewTransactions(categoryItem) }),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(text = "Name: ${categoryItem.name}")

        Text(text = "Total Amount: ${categoryItem.totalAmount}")

        OutlinedButton(
            onClick = { onViewTransactions(categoryItem) }
        ) {
            Text(text = "View Transactions")
        }
    }
}

@Preview
@Composable
fun CategoryItemPreview() {
    val sampleCategory = CategoryItem(
        id = 1,
        name = "Food",
        totalAmount = 500.0,
        enabled = true
    )
    Category(categoryItem = sampleCategory, onViewTransactions = {})
}