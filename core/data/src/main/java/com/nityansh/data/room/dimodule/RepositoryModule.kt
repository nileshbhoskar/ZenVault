package com.nityansh.data.room.dimodule

import android.content.Context
import androidx.room.Room
import com.nityansh.data.room.dao.CategoryDao
import com.nityansh.data.room.dao.TransactionDao
import com.nityansh.data.room.database.AppDatabase
import com.nityansh.data.room.repository.CategoryRepositoryImpl
import com.nityansh.data.room.repository.TransactionRepositoryImpl
import com.nityansh.domain.repository.CategoryRepository
import com.nityansh.domain.repository.TransactionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    /*@Provides
    @Singleton
    fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository {
        return transactionRepositoryImpl
    }

    @Provides
    @Singleton
    fun bindCategoryRepository(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository {
        return categoryRepositoryImpl
    }*/


    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "zenvault_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.expenseDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }
}