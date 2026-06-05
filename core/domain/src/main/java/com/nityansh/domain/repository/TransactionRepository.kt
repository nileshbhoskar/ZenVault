package com.nityansh.domain.repository

import com.nityansh.domain.models.TransactionItem
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<TransactionItem>>
    fun getAllTransactions(categoryId: Int): Flow<List<TransactionItem>>
    fun getTransactionByIdWithFlow(transactionId: Int): Flow<TransactionItem>
    fun getTransactionById(transactionId: Int): TransactionItem
    suspend fun addTransaction(transaction: TransactionItem): Long
    suspend fun updateTransaction(transaction: TransactionItem): Long
    fun deleteTransaction(transactionId: Int): Int
    suspend fun getTotalAmount(): Flow<Double>
    suspend fun getPerCategoryTotalAmount(): Flow<Map<Int, Double>>
}