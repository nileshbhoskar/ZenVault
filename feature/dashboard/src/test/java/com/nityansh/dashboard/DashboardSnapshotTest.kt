package com.nityansh.dashboard

import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.nityansh.dashboard.viewmodel.DashboardViewModel
import com.nityansh.domain.models.CategoryItem
import com.nityansh.domain.repository.CategoryRepository
import com.nityansh.domain.repository.TransactionRepository
import com.nityansh.domain.usecase.CategoryOperationsUsecase
import com.nityansh.domain.usecase.TotalExpenseUseCase
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DashboardSnapshotTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    val categoryRepository = mockk<CategoryRepository>()
    val transactionRepository = mockk<TransactionRepository>()
    val categoryUseCase = CategoryOperationsUsecase(categoryRepository)
    val totalUseCase = TotalExpenseUseCase(transactionRepository)


    lateinit var spyViewModel : DashboardViewModel
    @get:Rule
    val paparazzi = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_5.copy(
            softButtons = false,
            screenHeight = 1280,
            screenWidth = 720
        ),
        theme = "Theme.Material3.DayNight.NoActionBar"
    )

    @Before
    fun setup() {
        val categoryItems = listOf(
            CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 500.0),
            CategoryItem(id = 2, name = "Transport", enabled = true, totalAmount = 2500.0)
        )
        coEvery { categoryUseCase.getAllCategories() } returns flowOf(categoryItems)

        spyViewModel = spyk(DashboardViewModel(
            categoryUseCase = categoryUseCase,
            totalExpenseUseCase = totalUseCase
        ))
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun snapCustomButton()= runTest(unconfinedTestDispatcher) {
        spyViewModel.newCategoryName = "Test category"
        spyViewModel.selectedOption = "Enabled"
        spyViewModel.addCategory()

        val categoryItemList = listOf(
            CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 500.0),
            CategoryItem(id = 2, name = "Transport", enabled = true, totalAmount = 2500.0)
        )
        runCurrent()
        coEvery { categoryRepository.addCategory(any()) } returns 1

        coEvery { categoryUseCase.getAllCategories() } returns flowOf(categoryItemList)

        paparazzi.snapshot {
            DashboardScreen(
                viewModel = spyViewModel,
                totalExpense = "200",
                onAddTransactionClick = {},
                onAddCategoryClick = {},
                onViewTransactions = {})
        }
    }
}