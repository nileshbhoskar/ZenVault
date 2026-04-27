package com.nityansh.domain.usecase

import com.nityansh.domain.repository.CategoryRepository
import com.nityansh.domain.repository.TransactionRepository
import javax.inject.Inject

class GetPerCategorySpendingUseCase @Inject constructor(val categoryRepository: CategoryRepository, val transactionRepository: TransactionRepository) {

     suspend fun invoke() = categoryRepository.getCategoryWiseTotalAmount()

}