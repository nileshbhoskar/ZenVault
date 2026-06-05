package com.nityansh.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
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

    Box {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TotalExpenses(viewModel.totalExpense)

            CategoryList(modifier = Modifier.weight(1f), viewModel = viewModel, onViewTransactions)
        }

        AddCategoryButton(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
                .semantics {
                    contentDescription = "Add Category Button"
                    role = Role.Button
                    heading()
                },
            onClick = onAddCategoryClick,
        )
    }
}

@Composable
fun AddCategoryButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier.wrapContentWidth(),
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
    DashboardScreen(
        viewModel = viewModel<DashboardViewModel>(),
        onAddCategoryClick = {},
        onAddTransactionClick = {},
        onViewTransactions = {})
}
