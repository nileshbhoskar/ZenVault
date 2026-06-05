package com.nityansh.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.nityansh.data.room.dao.CategoryDao
import com.nityansh.data.room.dao.TransactionDao
import com.nityansh.data.room.database.AppDatabase
import com.nityansh.data.room.entity.CategoryEntity
import com.nityansh.data.room.repository.CategoryRepositoryImpl
import com.nityansh.data.room.repository.TransactionRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CategoryDaoInstrumentedTest {

    private lateinit var database: AppDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var categoryRepositoryImpl: CategoryRepositoryImpl
    private lateinit var transactionRepositoryImpl: TransactionRepositoryImpl

    @OptIn(ExperimentalCoroutinesApi::class)
    val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    val stdTestDispatcher = StandardTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
//            .allowMainThreadQueries()
            .setQueryExecutor(unconfinedTestDispatcher.asExecutor())
            .setTransactionExecutor(unconfinedTestDispatcher.asExecutor())
            .build()
        categoryDao = database.categoryDao()
        transactionDao = database.transactionDao()
        categoryRepositoryImpl = CategoryRepositoryImpl(categoryDao, transactionDao)
        transactionRepositoryImpl = TransactionRepositoryImpl(transactionDao)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetAllCategories() = runTest(unconfinedTestDispatcher) {
        val category = CategoryEntity(id = 1, name = "Food", enabled = true)
        categoryDao.addCategory(category)

        categoryDao.getAllCategories().test {
            val emittedList = this.awaitItem()

            assertEquals(1, emittedList.size)
            assertEquals("Food", emittedList[0].name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testGetCategoriesById() = runTest(unconfinedTestDispatcher) {
        val category = CategoryEntity(id = 1, name = "Food", enabled = true)
        categoryDao.addCategory(category)

        val receivedCategory = categoryDao.getCategoryById(1)

        assertEquals(category.id, receivedCategory?.id)
        assertEquals(category.name, receivedCategory?.name)
        assertEquals(category.enabled, receivedCategory?.enabled)
    }

    @Test
    fun testInsertCategories() = runTest(unconfinedTestDispatcher) {
        val category = CategoryEntity(id = 2, name = "Grocery", enabled = true)
        val count = categoryDao.addCategory(category)
        assertEquals(2, count)
    }

    @Test
    fun testUpsertCategories() = runTest(unconfinedTestDispatcher) {
        val category = CategoryEntity(id = 2, name = "Grocery", enabled = false)
        val id: Long = categoryDao.upsertCategory(category)
        assertEquals(2L, id)
        categoryDao.getAllCategories().test {
            val emittedList = awaitItem()

            assertEquals(1, emittedList.size)
            assertEquals(false, emittedList[0].enabled)

            cancelAndIgnoreRemainingEvents()
        }
    }
}