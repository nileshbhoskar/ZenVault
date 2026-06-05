package com.nityansh.domain.models

import org.junit.Assert.assertNotEquals
import org.junit.Test

class CategoryItemTest {

    @Test
    fun testCategoryItemCreation() {
        val categoryFood = CategoryItem(
            id = 1,
            name = "Food",
            totalAmount = 150.0,
            enabled = true
        )

        assert(categoryFood.id == 1)
        assert(categoryFood.name == "Food")
        assert(categoryFood.totalAmount == 150.0)
        assert(categoryFood.enabled)
        val categoryGrocery = CategoryItem(
            id = 1,
            name = "Grocery",
            totalAmount = 150.0,
            enabled = true
        )
        assertNotEquals(categoryGrocery, categoryFood)
    }
}