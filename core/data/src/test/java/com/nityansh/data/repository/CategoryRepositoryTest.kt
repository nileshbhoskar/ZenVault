package com.nityansh.data.repository

import app.cash.turbine.test
import com.nityansh.data.room.dao.CategoryDao
import com.nityansh.data.room.dao.TransactionDao
import com.nityansh.data.room.entity.CategoryEntity
import com.nityansh.data.room.entity.CategoryTotal
import com.nityansh.data.room.entity.ExpenseEntity
import com.nityansh.data.room.repository.CategoryRepositoryImpl
import com.nityansh.domain.models.CategoryItem
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CategoryRepositoryTest {

    private val categoryDao: CategoryDao = mockk()
    private val transactionDao: TransactionDao = mockk(relaxed = true)
    private val repository = CategoryRepositoryImpl(categoryDao, transactionDao)

    @Test
    fun `verify add category`() = runTest {
        val category = CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 0.0)

        coEvery{ repository.addCategory(category) } returns 0L
        val result = repository.addCategory(category)
        assertEquals(0L, result)
    }

    @Test
    fun `verify update category`() = runTest{
        val category = CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 0.0)
        coEvery{ repository.updateCategory(category) } returns 0L
        val result = repository.updateCategory(category)
        assertEquals(0L, result)
    }

    @Test
    fun `getOfflineCategories return data from Dao`() = runTest {
        val categoryItems = listOf(
            CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 500.0),
            CategoryItem(id = 2, name = "Transport", enabled = true, totalAmount = 2500.0)
        )
        val categoryEntities = listOf(
            CategoryEntity(id = 1, name = "Food", enabled = true),
            CategoryEntity(id = 2, name = "Transport", enabled = true)
        )
//        val dbEntities = flowOf()
        every { repository.getAllCategories() } returns flowOf(categoryItems)

        every { categoryDao.getAllCategories() } returns flowOf(categoryEntities)

        val categoryTotals = listOf(
            CategoryTotal(id = 1, totalAmount = 500.0),
            CategoryTotal(id = 2, totalAmount = 2500.0)
        )
        coEvery { transactionDao.getExpensesSumByCategory() } returns flowOf(categoryTotals)

        repository.getAllCategories().test {
            val emittedCategories = awaitItem()

            assertEquals(2, emittedCategories.size)
            assertEquals("Food", emittedCategories[0].name)

            awaitComplete()
        }
    }

    @Test
    fun `getOfflineCategories return empty list when Dao returns empty`() = runTest {
        every { repository.getAllCategories() } returns flowOf(emptyList())

        every { categoryDao.getAllCategories() } returns flowOf(emptyList())

        coEvery { transactionDao.getExpensesSumByCategory() } returns flowOf(emptyList())

        repository.getAllCategories().test {
            val emittedCategories = awaitItem()

            assertEquals(0, emittedCategories.size)

            awaitComplete()
        }
    }

    @Test
    fun `getCategoryWiseTotalAmount return data from Dao`() = runTest {
        val list = listOf(
            CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 500.0),
            CategoryItem(id = 2, name = "Transport", enabled = true, totalAmount = 2500.0),
        )
        val entityList = listOf(
            CategoryEntity(id = 1, name = "Food", enabled = true),
            CategoryEntity(id = 2, name = "Transport", enabled = true),
        )
        val map: HashMap<CategoryItem, Double> = HashMap(list.size)
        list[0] to 500.0
        list[1] to 2500.0

        coEvery { categoryDao.getAllCategories() } returns flowOf(entityList)
        coEvery { transactionDao.getAllExpensesByCategory(1) } returns flowOf(
            listOf(
                ExpenseEntity(id = 1, amount = 500.0, categoryId = 1, description = "Dinner", date = System.currentTimeMillis()),
                ExpenseEntity(id = 2, amount = 2500.0, categoryId = 1, description = "Lunch", date = System.currentTimeMillis())
            )
        )

        coEvery { repository.getCategoryWiseTotalAmount() } returns flowOf(map)

        repository.getCategoryWiseTotalAmount().test{
            val mapOfCategoryTotal = awaitItem()
            assertEquals(map as Map<CategoryItem, Double>, mapOfCategoryTotal)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
