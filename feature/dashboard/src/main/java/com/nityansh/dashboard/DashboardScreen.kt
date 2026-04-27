package com.nityansh.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nityansh.dashboard.components.CategoryList
import com.nityansh.dashboard.components.TotalExpenses
import com.nityansh.dashboard.viewmodel.DashboardViewModel
import com.nityansh.domain.models.CategoryItem

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onAddTransactionClick: () -> Unit,
    onAddCategoryClick: () -> Unit,
    onViewTransactions: (CategoryItem) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TotalExpenses(viewModel.totalExpense)

        CategoryList(modifier = Modifier.weight(1f), viewModel = viewModel, onViewTransactions)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.weight(1f))
            AddCategoryButton(onClick = onAddCategoryClick)
            Spacer(modifier = Modifier.weight(1f))
            AddTransactionButton(onClick = onAddTransactionClick)
        }
    }
}

@Composable
fun AddCategoryButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    OutlinedButton(
        modifier = modifier.wrapContentWidth(),
        onClick = {
            onClick.invoke()
        },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
    ) {
        Text(
            modifier = Modifier.background(MaterialTheme.colorScheme.primary),
            text = "Add Category"
        )
    }
}

@Composable
fun AddTransactionButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier.wrapContentWidth().padding(end = 16.dp),
        onClick = {
            onClick.invoke()
        },
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = Color.White
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add Category"
        )
    }
}

@Preview
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(viewModel = viewModel<DashboardViewModel>(), onAddCategoryClick = {}, onAddTransactionClick = {},
        onViewTransactions = {})
}
