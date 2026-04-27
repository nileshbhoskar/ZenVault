package com.nityansh.data.room.dimodule

import com.nityansh.data.room.repository.CategoryRepositoryImpl
import com.nityansh.data.room.repository.TransactionRepositoryImpl
import com.nityansh.domain.repository.CategoryRepository
import com.nityansh.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class TestRepoModule {

    @Binds
    @Singleton
    abstract fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository


}