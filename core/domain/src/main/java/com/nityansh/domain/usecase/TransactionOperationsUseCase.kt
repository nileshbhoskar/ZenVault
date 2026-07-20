package com.nityansh.domain.usecase

import com.nityansh.domain.models.TransactionItem
import com.nityansh.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionOperationsUseCase @Inject constructor(val transactionRepository: TransactionRepository) {

    fun getAllTransactionList() = transactionRepository.getAllTransactions()

    fun getAllTransactionList(categoryId: Int) =
        transactionRepository.getAllTransactions(categoryId)

    suspend fun getTransactionById(transactionId: Int) =
        transactionRepository.getTransactionById(transactionId)

    fun getTransactionByIdWithFlow(transactionId: Int) =
        transactionRepository.getTransactionByIdWithFlow(transactionId)

    suspend fun addTransaction(transactionItem: TransactionItem) =
        transactionRepository.addTransaction(transactionItem)

    suspend fun updateTransaction(transactionItem: TransactionItem) =
        transactionRepository.updateTransaction(transactionItem)

    suspend fun deleteTransaction(transactionId: Int) =
        transactionRepository.deleteTransaction(transactionId)
}