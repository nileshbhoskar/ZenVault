package com.nityansh.dashboard.state

data class AddCategoryState(
    val categoryName: String = "",
    val isEnabled: Boolean? = null,
    val categoryNameError: String = "Enter valid category name",
    val categoryStatusError: String = "Select category status",
    val showCategoryNameError: Boolean = false,
    val showCategoryStatusError: Boolean = false
)