package com.nityansh.dashboard.intent

sealed class DashboardUiIntent {
    object Inactive: DashboardUiIntent()
    object Loading: DashboardUiIntent()
    data class Success(
        val categoryIntent: CategoryIntent,
        val transactionIntent: TransactionIntent
    ): DashboardUiIntent()
    data class Error(
        val message: String
    ): DashboardUiIntent()
}