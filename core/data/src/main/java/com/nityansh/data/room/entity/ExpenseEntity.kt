package com.nityansh.data.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "amount")
    val amount: Double,
    @ColumnInfo(name = "category_id", defaultValue = "0")
    val categoryId: Int,
    @ColumnInfo(name = "date")
    val date: Long,
    @ColumnInfo(name = "description")
    val description: String
)
