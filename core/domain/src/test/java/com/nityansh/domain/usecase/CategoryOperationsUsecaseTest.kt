package com.nityansh.domain.usecase

import com.nityansh.domain.models.CategoryItem
import com.nityansh.domain.repository.CategoryRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Test

class CategoryOperationsUsecaseTest {

    val mockkRepository: CategoryRepository = mockk()
    val spyRepository = spyk<CategoryRepository>()

    val useCase = CategoryOperationsUsecase(mockkRepository)
    val spyUseCase = CategoryOperationsUsecase(spyRepository)

    @Test
    fun `test get all categories`() {

        val categoryFood =
            flowOf(listOf(CategoryItem(id = 1, name = "Food", totalAmount = 150.0, enabled = true)))
        every { useCase.getAllCategories() } returns categoryFood

        val result = useCase.getAllCategories()
        assertEquals(categoryFood, result)
    }

    @Test
    fun `test get all categories with spy`() = runTest {
        val categoryFood =
            flowOf(listOf(CategoryItem(id = 1, name = "Food", totalAmount = 150.0, enabled = true)))
        every { spyUseCase.getAllCategories() } returns categoryFood

        val result = spyUseCase.getAllCategories()
        assertEquals(categoryFood, result)
    }

    @Test
    fun `execute throw exception when repository fails`() = runTest {
        val exceptionMessage = "DB error occurred"
        coEvery { mockkRepository.getAllCategories() } throws Exception(exceptionMessage)

        val exception = assertThrows(Exception::class.java) {
            runTest {
                useCase.getAllCategories()
            }
        }
        assertEquals(exceptionMessage, exception.message)
    }
}