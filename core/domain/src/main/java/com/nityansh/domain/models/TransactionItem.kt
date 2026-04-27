package com.nityansh.domain.models

import java.text.SimpleDateFormat
import java.util.Locale

data class TransactionItem(
    val id: Int,
    val amount: Double,
    val date: Long,
    val description: String,
    val categoryId: Int
) {

    val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
}
