package com.nityansh.data.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.nityansh.data.room.dao.CategoryDao
import com.nityansh.data.room.dao.TransactionDao
import com.nityansh.data.room.entity.CategoryEntity
import com.nityansh.data.room.entity.ExpenseEntity


@Database(entities = [ExpenseEntity::class, CategoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun expenseDao(): TransactionDao

    abstract fun categoryDao(): CategoryDao
}