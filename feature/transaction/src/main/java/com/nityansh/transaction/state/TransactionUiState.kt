package com.nityansh.transaction.state

import com.nityansh.domain.models.TransactionItem

data class TransactionUiState(
    val isLoading: Boolean = false,
    val transactions: List<TransactionItem> = emptyList(),
    val error: String? = null,
)