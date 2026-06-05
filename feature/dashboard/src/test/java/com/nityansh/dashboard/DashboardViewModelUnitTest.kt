package com.nityansh.dashboard

import com.nityansh.dashboard.viewmodel.DashboardViewModel
import com.nityansh.domain.models.CategoryItem
import com.nityansh.domain.repository.CategoryRepository
import com.nityansh.domain.repository.TransactionRepository
import com.nityansh.domain.usecase.CategoryOperationsUsecase
import com.nityansh.domain.usecase.TotalExpenseUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DashboardViewModelUnitTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    val categoryRepository = mockk<CategoryRepository>()
    val transactionRepository = mockk<TransactionRepository>()
    val categoryUseCase = CategoryOperationsUsecase(categoryRepository)
    val totalUseCase = TotalExpenseUseCase(transactionRepository)

    val viewModel =
        DashboardViewModel(categoryUseCase = categoryUseCase, totalExpenseUseCase = totalUseCase)
    val spyViewModel = spyk(viewModel)

    @Test
    fun `add new category test with success`() = runTest(unconfinedTestDispatcher) {
        spyViewModel.newCategoryName = "Test category"
        spyViewModel.selectedOption = "Enabled"
        spyViewModel.addCategory()

        val categoryItemList = listOf(
            CategoryItem(
                id = 1,
                name = "Test category",
                totalAmount = 0.0,
                enabled = true
            )
        )
        runCurrent()
        coEvery { categoryRepository.addCategory(any()) } returns 1

        coEvery { categoryUseCase.getAllCategories() } returns flowOf(categoryItemList)

        coVerify(exactly = 1) {categoryRepository.addCategory(any())}
    }
}