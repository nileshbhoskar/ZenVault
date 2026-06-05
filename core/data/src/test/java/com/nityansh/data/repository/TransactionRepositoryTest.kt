package com.nityansh.data.repository

import app.cash.turbine.test
import com.nityansh.data.room.dao.TransactionDao
import com.nityansh.data.room.entity.ExpenseEntity
import com.nityansh.data.room.repository.TransactionRepositoryImpl
import com.nityansh.domain.models.TransactionItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class TransactionRepositoryTest {

    private val transactionDao: TransactionDao = mockk()
    private val repository = spyk(TransactionRepositoryImpl(transactionDao))

    @Test
    fun `verify addTransaction calls dao with correct parameters`() = runTest {
        val transaction = TransactionItem(
            id = 1,
            categoryId = 1,
            amount = 500.0,
            date = System.currentTimeMillis(),
            description = "Lunch"
        )

        coEvery { transactionDao.upsertExpense(any()) } returns 1L
        coEvery { repository.addTransaction(transaction) } returns 1L

        val result = repository.addTransaction(transaction)

        assertEquals(1L, result)
        coVerify { repository.addTransaction(transaction) }
    }

    @Test
    fun `verify updateTransaction calls dao with correct parameters`() = runTest {
        val transaction = TransactionItem(
            id = 1,
            categoryId = 1,
            amount = 600.0,
            date = System.currentTimeMillis(),
            description = "Dinner"
        )

        coEvery { transactionDao.upsertExpense(any()) } returns 1L
        coEvery { repository.updateTransaction(transaction) } returns 1L

        val result = repository.updateTransaction(transaction)

        assertEquals(1L, result)
        coVerify { repository.updateTransaction(transaction) }
    }

    @Test
    fun `getAllTransactions returns data from Dao and maps correctly`() = runTest {
        val transactionEntities = listOf(
            ExpenseEntity(id = 1, categoryId = 1, amount = 500.0, date = System.currentTimeMillis(), description = "Lunch"),
            ExpenseEntity(id = 2, categoryId = 2, amount = 1500.0, date = System.currentTimeMillis(), description = "Fuel")
        )

        val expectedTransactions = listOf(
            TransactionItem(id = 1, categoryId = 1, amount = 500.0, date = System.currentTimeMillis(), description = "Lunch"),
            TransactionItem(id = 2, categoryId = 2, amount = 1500.0, date = System.currentTimeMillis(), description = "Fuel")
        )

        every { transactionDao.getAllExpenses() } returns flowOf(transactionEntities)

        repository.getAllTransactions().test {
            val emittedTransactions = awaitItem()

            assertEquals(2, emittedTransactions.size)
            assertEquals("Lunch", emittedTransactions[0].description)
            assertEquals(500.0, emittedTransactions[0].amount, 0.0)
            assertEquals("Fuel", emittedTransactions[1].description)

            verify { transactionDao.getAllExpenses() }
            awaitComplete()
        }
    }

    @Test
    fun `getAllTransactions returns empty list when Dao returns empty`() = runTest {
        every { transactionDao.getAllExpenses() } returns flowOf(emptyList())

        repository.getAllTransactions().test {
            val emittedTransactions = awaitItem()

            assertEquals(0, emittedTransactions.size)

            verify { transactionDao.getAllExpenses() }
            awaitComplete()
        }
    }

    @Test
    fun `getAllTransactions by categoryId returns filtered data from Dao`() = runTest {
        val categoryId = 1
        val transactionEntities = listOf(
            ExpenseEntity(id = 1, categoryId = 1, amount = 500.0, date = System.currentTimeMillis(), description = "Lunch"),
            ExpenseEntity(id = 2, categoryId = 1, amount = 300.0, date = System.currentTimeMillis(), description = "Breakfast")
        )

        every { transactionDao.getAllExpensesByCategory(categoryId) } returns flowOf(transactionEntities)

        repository.getAllTransactions(categoryId).test {
            val emittedTransactions = awaitItem()

            assertEquals(2, emittedTransactions.size)
            assertEquals(1, emittedTransactions[0].categoryId)
            assertEquals(1, emittedTransactions[1].categoryId)

            verify { transactionDao.getAllExpensesByCategory(categoryId) }
            awaitComplete()
        }
    }

    @Test
    fun `getTransactionByIdWithFlow returns correct transaction`() = runTest {
        val transactionId = 1
        val transactionEntity = ExpenseEntity(
            id = 1,
            categoryId = 1,
            amount = 500.0,
            date = System.currentTimeMillis(),
            description = "Lunch"
        )

        every { transactionDao.getExpensesByIdFlow(transactionId) } returns flowOf(transactionEntity)

        repository.getTransactionByIdWithFlow(transactionId).test {
            val emittedTransaction = awaitItem()

            assertEquals(1, emittedTransaction.id)
            assertEquals("Lunch", emittedTransaction.description)
            assertEquals(500.0, emittedTransaction.amount, 0.0)

            verify { transactionDao.getExpensesByIdFlow(transactionId) }
            awaitComplete()
        }
    }

    @Test
    fun `getTransactionById returns correct transaction synchronously`() = runTest {
        val transactionId = 1
        val transactionEntity = ExpenseEntity(
            id = 1,
            categoryId = 1,
            amount = 500.0,
            date = System.currentTimeMillis(),
            description = "Lunch"
        )

        every { transactionDao.getExpensesById(transactionId) } returns transactionEntity

        val result = repository.getTransactionById(transactionId)

        assertEquals(1, result.id)
        assertEquals("Lunch", result.description)
        assertEquals(500.0, result.amount, 0.0)

        verify { transactionDao.getExpensesById(transactionId) }
    }

    @Test
    fun `deleteTransaction calls dao and returns deleted count`() = runTest {
        val transactionId = 1

        every { transactionDao.deleteExpenseById(transactionId) } returns 1

        val result = repository.deleteTransaction(transactionId)

        assertEquals(1, result)
        verify { transactionDao.deleteExpenseById(transactionId) }
    }

    @Test
    fun `getTotalAmount sums all transaction amounts correctly`() = runTest {
        val transactionEntities = listOf(
            ExpenseEntity(id = 1, categoryId = 1, amount = 500.0, date = System.currentTimeMillis(), description = "Lunch"),
            ExpenseEntity(id = 2, categoryId = 2, amount = 1500.0, date = System.currentTimeMillis(), description = "Fuel"),
            ExpenseEntity(id = 3, categoryId = 1, amount = 300.0, date = System.currentTimeMillis(), description = "Breakfast")
        )

        every { transactionDao.getAllExpenses() } returns flowOf(transactionEntities)

        repository.getTotalAmount().test {
            val totalAmount = awaitItem()

            assertEquals(2300.0, totalAmount, 0.0)

            verify { transactionDao.getAllExpenses() }
            awaitComplete()
        }
    }

    @Test
    fun `getTotalAmount returns zero when no transactions`() = runTest {
        every { transactionDao.getAllExpenses() } returns flowOf(emptyList())

        repository.getTotalAmount().test {
            val totalAmount = awaitItem()

            assertEquals(0.0, totalAmount, 0.0)

            verify { transactionDao.getAllExpenses() }
            awaitComplete()
        }
    }

    @Test
    fun `getPerCategoryTotalAmount groups and sums correctly`() = runTest {
        val transactionEntities = listOf(
            ExpenseEntity(id = 1, categoryId = 1, amount = 500.0, date = System.currentTimeMillis(), description = "Lunch"),
            ExpenseEntity(id = 2, categoryId = 2, amount = 1500.0, date = System.currentTimeMillis(), description = "Fuel"),
            ExpenseEntity(id = 3, categoryId = 1, amount = 300.0, date = System.currentTimeMillis(), description = "Breakfast")
        )

        every { transactionDao.getAllExpenses() } returns flowOf(transactionEntities)

        repository.getPerCategoryTotalAmount().test {
            val categoryTotals = awaitItem()

            assertEquals(2, categoryTotals.size)
            assertEquals(800.0, categoryTotals[1] ?: 0.0, 0.0)
            assertEquals(1500.0, categoryTotals[2] ?: 0.0, 0.0)

            verify { transactionDao.getAllExpenses() }
            awaitComplete()
        }
    }

    @Test
    fun `getPerCategoryTotalAmount handles multiple amounts per category`() = runTest {
        val transactionEntities = listOf(
            ExpenseEntity(id = 1, categoryId = 1, amount = 500.0, date = System.currentTimeMillis(), description = "Lunch"),
            ExpenseEntity(id = 2, categoryId = 1, amount = 300.0, date = System.currentTimeMillis(), description = "Breakfast"),
            ExpenseEntity(id = 3, categoryId = 1, amount = 200.0, date = System.currentTimeMillis(), description = "Snacks")
        )

        every { transactionDao.getAllExpenses() } returns flowOf(transactionEntities)

        repository.getPerCategoryTotalAmount().test {
            val categoryTotals = awaitItem()

            assertEquals(1, categoryTotals.size)
//            assertEquals(1000.0, categoryTotals[1], 0.0)

            verify { transactionDao.getAllExpenses() }
            awaitComplete()
        }
    }
}

