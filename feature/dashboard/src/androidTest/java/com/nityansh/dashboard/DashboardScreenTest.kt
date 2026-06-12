package com.nityansh.dashboard

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nityansh.dashboard.ui.components.AddCategoryDialog
import com.nityansh.dashboard.FakeCategoryRepository
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
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class DashboardScreenTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule(ComponentActivity::class.java)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val unconfinedTestDispatcher = UnconfinedTestDispatcher()
    val categoryRepository: CategoryRepository = spyk<FakeCategoryRepository>()
    val transactionRepository = mockk<TransactionRepository>()
    val categoryUseCase = CategoryOperationsUsecase(categoryRepository)
    val totalUseCase = TotalExpenseUseCase(transactionRepository)

    lateinit var viewModel: DashboardViewModel

    @Before
    fun setup() {
        val categoryItems = listOf(
            CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 500.0),
            CategoryItem(id = 2, name = "Transport", enabled = true, totalAmount = 2500.0)
        )
        coEvery { categoryUseCase.getAllCategories() } returns flowOf(categoryItems)

        viewModel = DashboardViewModel(
            categoryUseCase = categoryUseCase,
            totalExpenseUseCase = totalUseCase
        )
    }

    @Test
    fun dashboardScreenPopulateShownTotalExpenseText() = runTest {
        val categoryItems = listOf(
            CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 500.0),
            CategoryItem(id = 2, name = "Transport", enabled = true, totalAmount = 2500.0)
        )

        coEvery { categoryUseCase.getAllCategories() } returns flowOf(categoryItems)

        coEvery { viewModel.totalExpenseUseCase.getTotalAmount() } returns flowOf(1000.0)
        coEvery { totalUseCase.getTotalAmount() } returns flowOf(1000.0)

        composeTestRule.setContent {
            DashboardScreen(
                viewModel = viewModel,
                onAddTransactionClick = {},
                onAddCategoryClick = {},
                onViewTransactions = {}
            )
        }

        composeTestRule.onNodeWithText("Total Expenses", substring = true).assertExists()
        composeTestRule.onNodeWithText("Total Expenses:\n1000.0").assertExists()
        composeTestRule.onNodeWithContentDescription("Total Expenses Displayed").assertExists()
    }

    @Test
    fun dashboardScreenShowsCategories() = runTest {
        val categoryItems = listOf(
            CategoryItem(id = 1, name = "Food", enabled = true, totalAmount = 500.0),
            CategoryItem(id = 2, name = "Transport", enabled = true, totalAmount = 2500.0)
        )

        coEvery { categoryUseCase.getAllCategories() } returns flowOf(categoryItems)
        coEvery { totalUseCase.getTotalAmount() } returns flowOf(1000.0)
        composeTestRule.setContent {
            DashboardScreen(
                viewModel = viewModel,
                onAddTransactionClick = {},
                onAddCategoryClick = {
                },
                onViewTransactions = {}
            )
        }
        Thread.sleep(5000)

        composeTestRule.onNodeWithText("Food", substring = true).assertExists()
        composeTestRule.onNodeWithText("Transport", substring = true).assertExists()
    }

    @Test
    fun dashboardScreenPerformAddCategoryClickAndShowDialog() = runTest {

        coEvery { totalUseCase.getTotalAmount() } returns flowOf(1000.0)
        composeTestRule.setContent {
            DashboardScreen(
                viewModel = viewModel,
                onAddTransactionClick = {},
                onAddCategoryClick = {
                    viewModel.showAddCategoryDialog = true
                },
                onViewTransactions = {}
            )
            if (viewModel.showAddCategoryDialog) {
                AddCategoryDialog(viewModel)
            }
        }

        val initialCount = composeTestRule.onAllNodesWithTag("list_item").fetchSemanticsNodes(true,"Invalid test tag").size
        composeTestRule.onNodeWithContentDescription("Add Category Button").performClick()

        composeTestRule.waitForIdle()

//        composeTestRule.onNodeWithTag("Add New Category Dialog").assertExists()
        composeTestRule.onNodeWithContentDescription("Category Name Input Field").assertExists()

        composeTestRule.onNodeWithContentDescription("Category Name Input Field").performTextInput("Health")
        composeTestRule.onNodeWithContentDescription("Title Status for Category Status Options").assertExists()
        composeTestRule.onNodeWithContentDescription("Enabled Option").assertExists()
        composeTestRule.onNodeWithContentDescription("Enabled Option").performClick()
        composeTestRule.onNodeWithContentDescription("Disabled Option").assertExists()
        composeTestRule.onNodeWithContentDescription("Add Buttons").assertExists()
        composeTestRule.onNodeWithText("Add").assertExists()
        composeTestRule.onNodeWithText("Cancel").assertExists()
        composeTestRule.onNodeWithContentDescription("Add Buttons").performClick()

        composeTestRule.awaitIdle()

        val newCount = composeTestRule.onAllNodesWithTag("list_item").fetchSemanticsNodes(true,"Invalid test tag").size
        assert(newCount == initialCount + 1)

        coVerify(exactly = 1) { categoryRepository.addCategory(any()) }
    }
}