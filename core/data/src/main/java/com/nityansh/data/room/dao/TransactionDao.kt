package com.nityansh.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.nityansh.data.room.entity.CategoryTotal
import com.nityansh.data.room.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Upsert
    fun upsertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expense")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Query("SELECT c.id, sum(amount) as totalAmount FROM category as c LEFT JOIN expense ON expense.category_id = c.id GROUP BY c.id")
    fun getExpensesSumByCategory(): Flow<List<CategoryTotal>>

    @Query("SELECT * FROM expense WHERE category_id = :categoryId")
    fun getAllExpensesByCategory(categoryId: Int): Flow<List<ExpenseEntity>>

    @Query("SELECT * FROM expense WHERE id = :transactionId")
    fun getExpensesById(transactionId: Int): ExpenseEntity

    @Query("SELECT * FROM expense WHERE id = :transactionId")
    fun getExpensesByIdFlow(transactionId: Int): Flow<ExpenseEntity>

    @Query("SELECT * FROM expense WHERE id = :id")
    fun getExpenseById(id: Int): ExpenseEntity?

    @Delete
    fun deleteExpense(expense: ExpenseEntity)

    @Query("Delete FROM expense where id = :id")
    fun deleteExpenseById(id: Int)
}
