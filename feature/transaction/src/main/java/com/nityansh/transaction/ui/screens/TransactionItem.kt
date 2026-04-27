package com.nityansh.transaction.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nityansh.domain.models.TransactionItem

@Composable
fun TransactionItem(
    category: String,
    item: TransactionItem,
    onEdit: (TransactionItem) -> Unit = {},
    onDelete: (TransactionItem) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .border(1.dp, Color.Black, shape = RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Category: $category", color = Color.Black)
        Text(text = "Description: ${item.description}", color = Color.Black)
        Text(text = "Date: ${item.formattedDate}", color = Color.Black)

        Row {
            Text(text = "Amount: ${item.amount}", color = Color.Black)
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .clickable(enabled = true, onClick = {
                        onEdit.invoke(item)
                    }),
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                tint = Color.Black
            )

            Icon(
                modifier = Modifier
                    .padding(start = 4.dp, end = 4.dp)
                    .clickable(enabled = true, onClick = {
                        onDelete.invoke(item)
                    }),
                imageVector = Icons.Default.Delete,
                contentDescription = "Edit",
                tint = Color.Black
            )
        }
    }
}

@Preview
@Composable
fun TransactionItemPreview() {
    TransactionItem(
        category = "Food",
        item = TransactionItem(
            id = 1,
            amount = 20.0,
            date = System.currentTimeMillis(),
            description = "Lunch at restaurant",
            categoryId = 1
        )
    )
}