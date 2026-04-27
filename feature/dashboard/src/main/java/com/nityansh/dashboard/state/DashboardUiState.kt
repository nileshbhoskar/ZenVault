package com.nityansh.dashboard.state

import com.nityansh.domain.models.TransactionItem

data class DashboardUiState(
    val addNewTransaction: Boolean = false,
    val transactionsList: List<TransactionItem> = emptyList(),
)
