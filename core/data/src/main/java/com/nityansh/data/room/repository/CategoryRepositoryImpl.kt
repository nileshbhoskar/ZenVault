package com.nityansh.data.room.repository

import com.nityansh.data.room.dao.CategoryDao
import com.nityansh.data.room.dao.TransactionDao
import com.nityansh.data.room.entity.CategoryEntity
import com.nityansh.domain.models.CategoryItem
import com.nityansh.domain.repository.CategoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    val categoryDao: CategoryDao, val transactionDao: TransactionDao
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<CategoryItem>> {
        val categorySumFlow = transactionDao.getExpensesSumByCategory()
        val categoriesFlow = categoryDao.getAllCategories()
        return categoriesFlow.zip(categorySumFlow) { categoryEntities, categoryTotals ->
            val categoryTotalMap = categoryTotals.associateBy { it.id }
            categoryEntities.map { categoryEntity ->
                val totalAmount = categoryTotalMap[categoryEntity.id]?.totalAmount ?: 0.0
                CategoryItem(
                    id = categoryEntity.id,
                    name = categoryEntity.name,
                    totalAmount = totalAmount,
                    enabled = categoryEntity.enabled
                )
            }
        }
    }

    override suspend fun addCategory(category: CategoryItem): Long {
        return categoryDao.addCategory(
            CategoryEntity(
                id = category.id, name = category.name, enabled = category.enabled
            )
        )
    }

    override suspend fun updateCategory(category: CategoryItem): Long {
        return categoryDao.upsertCategory(
            CategoryEntity(
                id = category.id, name = category.name, enabled = category.enabled
            )
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCategoryWiseTotalAmount(): Flow<Map<CategoryItem, Double>> {
        return categoryDao.getAllCategories().flatMapLatest { categoryEntities ->
            if (categoryEntities.isEmpty()) {
                return@flatMapLatest flowOf(emptyMap())
            }

            val expenseFlow = categoryEntities.map { entity ->
                transactionDao.getAllExpensesByCategory(entity.id).map { expenses ->
                    val totalAmount = expenses.sumOf { it.amount }
                    CategoryItem(
                        id = entity.id, name = entity.name, totalAmount = totalAmount, enabled = entity.enabled
                    ) to totalAmount
                }
            }

            combine(expenseFlow) {
                it.toMap()
            }
        }
    }
}