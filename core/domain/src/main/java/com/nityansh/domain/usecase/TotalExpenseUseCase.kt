package com.nityansh.domain.usecase

import com.nityansh.domain.repository.TransactionRepository
import javax.inject.Inject

class TotalExpenseUseCase @Inject constructor(val transactionRepository: TransactionRepository) {

    suspend fun getTotalAmount() = transactionRepository.getTotalAmount()

    suspend fun getPerCategoryTotalAmount() = transactionRepository.getPerCategoryTotalAmount()
}