package com.nityansh.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.nityansh.data.room.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Upsert
    fun upsertCategory(category: CategoryEntity): Long

    @Insert
    fun addCategory(category: CategoryEntity): Long

    @Query("SELECT * FROM category")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategoryById(id: Int): CategoryEntity?

    @Delete
    fun deleteCategory(category: CategoryEntity): Int

    @Query("Delete FROM category where id = :id")
    fun deleteCategoryById(id: Int): Int
}