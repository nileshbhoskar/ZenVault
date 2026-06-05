package com.nityansh.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TotalExpenses(totalExpense: String = "0") {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .wrapContentWidth()
            .height(124.dp)
            .padding(16.dp)
            .border(width = 1.dp, color = Color.Black, shape = CircleShape)
            .background(color = Color.White, shape = CircleShape)
            .padding(16.dp)
    ) {
        Text(
            text = "Total Expenses:\n$totalExpense", color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.semantics{
                contentDescription = "Total Expenses Displayed"
            }
        )
    }
}

@Preview
@Composable
fun TotalExpensesPreview() {
    TotalExpenses(totalExpense = "1500")
}