package com.nityansh.transaction.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nityansh.domain.models.CategoryItem
import com.nityansh.domain.models.TransactionItem
import com.nityansh.domain.usecase.CategoryOperationsUsecase
import com.nityansh.domain.usecase.TransactionOperationsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    categoryUseCase: CategoryOperationsUsecase,
    val transactionUseCase: TransactionOperationsUseCase
) : ViewModel() {


    private val categoryId = savedStateHandle.get<Int?>("categoryId")
    val receivedCategoryName = savedStateHandle.get<String?>("categoryName")

    val transactionId = savedStateHandle.get<Int?>("transactionId")
    val transactionsState = if (categoryId != null) {
        transactionUseCase.getAllTransactionList(categoryId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), emptyList())
    } else {
        transactionUseCase.getAllTransactionList()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), emptyList())
    }
    val categories = categoryUseCase.getAllCategories()
        .onEach { list ->
            selectedCategoryName =
                list.firstOrNull { it.id == selectedTransaction?.categoryId }?.name
            selectedCategory = list.firstOrNull { it.id == selectedTransaction?.categoryId }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(2000), emptyList())
    var selectedCategory: CategoryItem? by mutableStateOf(null)
    var selectedCategoryName: String? by mutableStateOf(null)
    var selectedTransaction: TransactionItem? by mutableStateOf(null)

    var description: String by mutableStateOf("")
    var transactionDate: String by mutableStateOf("")
    var transactionDateInMilli: Long by mutableLongStateOf(0L)
    var selectCategoryError: Boolean by mutableStateOf(false)
    var amount: String by mutableStateOf("")

    var descriptionError: Boolean by mutableStateOf(false)
    var dateError: Boolean by mutableStateOf(false)
    var amountError: Boolean by mutableStateOf(false)

    var categoryDropDownExpanded by mutableStateOf(false)
    var deleteTransaction: TransactionItem? by mutableStateOf(null)
    var showDeleteTransactionDialog by mutableStateOf(false)

    init {
        if (transactionId != null && transactionId != -1) {
            viewModelScope.launch(Dispatchers.IO) {
                val result = transactionUseCase.getTransactionById(transactionId)
                result.let { transaction ->
                    withContext(Dispatchers.Main) {
                        Log.d("TransactionViewModel", "amount: $selectedTransaction")
                        selectedTransaction = transaction
                        description = transaction.description
                        transactionDateInMilli = transaction.date

                        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        transactionDate = sdf.format(transactionDateInMilli)

                        amount = transaction.amount.toString()
//                        selectedCategoryName = categories.value.firstOrNull { it.id == transaction.categoryId }?.name
//                        selectedCategory = categories.value.firstOrNull { it.id == transaction.categoryId }
                    }
                }
            }
        }
    }

    fun storeTransaction() {
        if (description.isBlank()) {
            descriptionError = true
            return
        }
        if (transactionDate.isBlank()) {
            dateError = true
            return
        }

        if (amount.isBlank()) {
            amountError = true
            return
        }
        if (selectedCategoryName?.isBlank() == true) {
            selectCategoryError = true
            return
        }
        val transactionItem = TransactionItem(

            id = if (transactionId == null || transactionId == -1) {
                0
            } else {
                transactionId
            },
            categoryId = selectedCategory?.id ?: 0,
            amount = amount.toDoubleOrNull() ?: 0.0,
            date = transactionDateInMilli,
            description = description
        )
        viewModelScope.launch(Dispatchers.Default) {
            if (transactionId == null) {
                transactionUseCase.addTransaction(transactionItem)
            } else {
                transactionUseCase.updateTransaction(transactionItem)
            }
        }
        clearTransaction()
    }

    fun clearTransaction() {
        description = ""
        transactionDate = ""
        amount = ""
        selectedCategoryName = null
        selectedCategory = null
        descriptionError = false
        dateError = false
        amountError = false
        selectCategoryError = false
    }

    fun removeTransaction(transactionId: Int) {
        viewModelScope.launch(Dispatchers.Default) {
            transactionUseCase.deleteTransaction(transactionId)
        }
    }
}