package com.nityansh.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.nityansh.data.room.dao.TransactionDao
import com.nityansh.data.room.database.AppDatabase
import com.nityansh.data.room.repository.TransactionRepositoryImpl
import com.nityansh.domain.models.TransactionItem
import com.nityansh.domain.repository.TransactionRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TransactionDaoInstrumentedTest {

    private lateinit var database: AppDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var transactionRepository: TransactionRepository

    @OptIn(ExperimentalCoroutinesApi::class)
    val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    val stdTestDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setQueryExecutor(unconfinedTestDispatcher.asExecutor())
            .setTransactionExecutor(unconfinedTestDispatcher.asExecutor())
            .build()

        transactionDao = database.transactionDao()
        transactionRepository = TransactionRepositoryImpl(transactionDao)
    }

    @After
    fun clearDb() {
        database.clearAllTables()
        database.close()
    }

    @Test
    fun testInsertAndGetTransaction() = runTest(context = unconfinedTestDispatcher) {
        val transaction = TransactionItem(
            id = 1,
            amount = 500.0,
            date = System.currentTimeMillis(),
            description = "Vegetable Market",
            categoryId = 1,
        )

        val transactionId = transactionRepository.addTransaction(transaction)
        assertEquals(1L, transactionId)

        val retrieveTransactions = transactionRepository.getAllTransactions()

        retrieveTransactions.test{
            val transactions = awaitItem()
            assertEquals(1, transactions.size)
            assertEquals("Vegetable Market", transactions[0].description)
            assertEquals(transaction, transactions[0])
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testInsertAndGetTransactionById() = runTest(context = unconfinedTestDispatcher) {
        val transactionFruit = TransactionItem(
            id = 5,
            amount = 500.0,
            date = System.currentTimeMillis(),
            description = "Fruit Market",
            categoryId = 1,
        )

        val transactionGrocery = TransactionItem(
            id = 2,
            amount = 1000.0,
            date = System.currentTimeMillis(),
            description = "Grocery",
            categoryId = 2,
        )

        val transactionVegies = TransactionItem(
            id = 3,
            amount = 1500.0,
            date = System.currentTimeMillis(),
            description = "Vegies Market",
            categoryId = 1,
        )

        val transactionShopping = TransactionItem(
            id = 4,
            amount = 2000.0,
            date = System.currentTimeMillis(),
            description = "Shopping Market",
            categoryId = 2,
        )

        val transactionFruitId = transactionRepository.addTransaction(transactionFruit)
        val transactionGroceryId = transactionRepository.addTransaction(transactionGrocery)
        val transactionVegiesId = transactionRepository.addTransaction(transactionVegies)
        val transactionShoppingId = transactionRepository.addTransaction(transactionShopping)
        assertEquals(5L, transactionFruitId)
        assertEquals(2L, transactionGroceryId)
        assertEquals(3L, transactionVegiesId)
        assertEquals(4L, transactionShoppingId)

        val retrieveTransactionsById = transactionRepository.getAllTransactions(2)

        retrieveTransactionsById.test{
            val transactions = awaitItem()
            assertEquals(2, transactions.size)
            assertEquals("Grocery", transactions[0].description)
            assertEquals("Shopping Market", transactions[1].description)
            assertEquals(2, transactions[1].categoryId)
            assertEquals(transactionShopping, transactions[1])
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testDeleteTransaction() = runTest(context = unconfinedTestDispatcher) {
        val transaction = TransactionItem(
            id = 1,
            amount = 500.0,
            date = System.currentTimeMillis(),
            description = "Vegetable Market",
            categoryId = 1,
        )
        val transactionId = transactionRepository.addTransaction(transaction)
        assertEquals(1L, transactionId)
    }

    @Test
    fun testUpdateTransaction() = runTest(context = unconfinedTestDispatcher) {
        val transaction = TransactionItem(
            id = 1,
            amount = 500.0,
            date = System.currentTimeMillis(),
            description = "Vegetable Market",
            categoryId = 1,
        )
        val transactionId = transactionRepository.updateTransaction(transaction)
        assertEquals(1L, transactionId)
    }


}