package com.nityansh.zenvault

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nityansh.dashboard.DashboardScreen
import com.nityansh.dashboard.ui.components.AddCategoryDialog
import com.nityansh.dashboard.viewmodel.DashboardViewModel
import com.nityansh.transaction.ui.screens.TransactionsScreen
import com.nityansh.transaction.ui.screens.UpdateInsertTransactionScreen

@Composable
fun MainScreen(dashboardViewModel: DashboardViewModel) {

    val navController = rememberNavController()
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "dashboard",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("dashboard") {
                DashboardScreen(dashboardViewModel, {
                    navController.navigate("upsert_transaction")
                }, {
                    dashboardViewModel.showAddCategoryDialog = true
                }, onViewTransactions = {
                    navController.navigate("transaction/${it.id}/${it.name}")
                })

                if (dashboardViewModel.showAddCategoryDialog) {
                    AddCategoryDialog(dashboardViewModel)
                }
            }

            composable(
                route = "transaction/{categoryId}/{categoryName}",
                arguments = listOf(navArgument("categoryId") {
                    type = NavType.IntType
                    nullable = false
                    defaultValue = -1
                }, navArgument("categoryName") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                })
            ) {
                TransactionsScreen() { transaction ->
                    navController.navigate("upsert_transaction?transactionId=${transaction.id}")
                }
            }

            composable(
                route = "upsert_transaction?transactionId={transactionId}",
                arguments = listOf(navArgument("transactionId") {
                    type = NavType.IntType
                    defaultValue = -1
                })
            ) {
                UpdateInsertTransactionScreen() {
                    navController.popBackStack()
                }
            }
        }
    }
}