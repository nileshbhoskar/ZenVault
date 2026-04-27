package com.nityansh.domain

data class Expense(
    val id: Int,
    val amount: Double,
    val date: String,
    val categoryId: Int,
    val description: String,
)
