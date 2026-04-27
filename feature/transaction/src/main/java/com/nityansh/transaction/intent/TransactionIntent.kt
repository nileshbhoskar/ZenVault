package com.nityansh.transaction.intent

import com.nityansh.domain.models.TransactionItem

sealed class TransactionIntent {
    object Inactive: TransactionIntent()
    object Loading: TransactionIntent()
    data class Success(
        val transactions: List<TransactionItem>
    ): TransactionIntent()
    data class Error(
        val message: String
    ): TransactionIntent()
}