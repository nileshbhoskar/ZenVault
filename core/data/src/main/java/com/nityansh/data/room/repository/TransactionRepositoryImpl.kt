package com.nityansh.data.room.repository

import com.nityansh.data.room.dao.TransactionDao
import com.nityansh.domain.models.TransactionItem
import com.nityansh.domain.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(val transactionDao: TransactionDao): TransactionRepository {
    override fun getAllTransactions(): Flow<List<TransactionItem>> {
        return transactionDao.getAllExpenses().map { transactionEntities ->
            transactionEntities.map { transactionEntity ->
                TransactionItem(
                    id = transactionEntity.id,
                    categoryId = transactionEntity.categoryId,
                    amount = transactionEntity.amount,
                    date = transactionEntity.date,
                    description = transactionEntity.description
                )
            }
        }
    }

    override fun getAllTransactions(categoryId: Int): Flow<List<TransactionItem>> {
        return transactionDao.getAllExpensesByCategory(categoryId).map { transactionEntities ->
            transactionEntities.map { transactionEntity ->
                TransactionItem(
                    id = transactionEntity.id,
                    categoryId = transactionEntity.categoryId,
                    amount = transactionEntity.amount,
                    date = transactionEntity.date,
                    description = transactionEntity.description
                )
            }
        }
    }

    override fun getTransactionByIdWithFlow(transactionId: Int): Flow<TransactionItem> {

        return transactionDao.getExpensesByIdFlow(transactionId).map { expense ->
            TransactionItem(
                id = expense.id,
                categoryId = expense.categoryId,
                amount = expense.amount,
                date = expense.date,
                description = expense.description
            )
        }
    }
    override suspend fun getTransactionById(transactionId: Int): TransactionItem = withContext(Dispatchers.IO) {
        val expense = transactionDao.getExpensesById(transactionId)
        return@withContext TransactionItem(
            id = expense.id,
            categoryId = expense.categoryId,
            amount = expense.amount,
            date = expense.date,
            description = expense.description
        )
    }

    override suspend fun addTransaction(transaction: TransactionItem): Long = withContext(Dispatchers.IO) {
        return@withContext transactionDao.upsertExpense(
            com.nityansh.data.room.entity.ExpenseEntity(
                id = transaction.id,
                categoryId = transaction.categoryId,
                amount = transaction.amount,
                date = transaction.date,
                description = transaction.description
            )
        )
    }

    override suspend fun updateTransaction(transaction: TransactionItem): Long = withContext(Dispatchers.IO) {
        return@withContext transactionDao.upsertExpense(
            com.nityansh.data.room.entity.ExpenseEntity(
                id = transaction.id,
                categoryId = transaction.categoryId,
                amount = transaction.amount,
                date = transaction.date,
                description = transaction.description
            )
        )
    }

    override suspend fun deleteTransaction(transactionId: Int): Int  = withContext(Dispatchers.IO) {
        return@withContext transactionDao.deleteExpenseById(transactionId)
    }

    override suspend fun getTotalAmount(): Flow<Double> = withContext(Dispatchers.IO){
        return@withContext transactionDao.getAllExpenses().map { transactionEntities ->
            transactionEntities.sumOf { it.amount }
        }
    }

    override suspend fun getPerCategoryTotalAmount(): Flow<Map<Int, Double>> = withContext(Dispatchers.IO) {
        return@withContext transactionDao.getAllExpenses().map { transactionEntities ->
            transactionEntities.groupBy { it.categoryId }.mapValues { entry ->
                entry.value.sumOf { it.amount }
            }
        }
    }
}