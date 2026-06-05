package com.nityansh.domain.models

import junit.framework.TestCase.assertEquals
import org.junit.Test

class TransactionItemTest {

    @Test
    fun testTransactionItemCreation() {
        // Create and test a TransactionItem instance
        val transaction1 = TransactionItem(
            id = 1,
            amount = 40.0,
            date = 1625097600000, // Example timestamp
            description = "Fruit shopping",
            categoryId = 2
        )
        val transaction2 = TransactionItem(
            id = 1,
            amount = 40.0,
            date = 1625097600000, // Example timestamp
            description = "Grocery shopping",
            categoryId = 2
        )

        assertEquals(1, transaction1.id)
        assertEquals(transaction2, transaction1)
        assertEquals("01-07-2021", transaction1.formattedDate)
    }
}