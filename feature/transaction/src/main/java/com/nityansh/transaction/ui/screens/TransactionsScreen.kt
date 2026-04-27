package com.nityansh.transaction.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nityansh.domain.models.TransactionItem
import com.nityansh.transaction.viewmodel.TransactionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    transactionViewModel: TransactionViewModel= hiltViewModel(),
    onEditClick: (TransactionItem) -> Unit = {}
) {

    val transactionsState by transactionViewModel.transactionsState.collectAsState()

    Column {

        Text(
            text = transactionViewModel.receivedCategoryName ?: "Transactions",
            color = Color.Black,
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            items(transactionsState.size) { index ->
                val transactionItem = transactionsState[index]
                TransactionItem(
                    category = transactionViewModel.receivedCategoryName ?: "Transactions",
                    item = transactionItem,
                    onEdit = {
                        onEditClick.invoke(it)
                    },

                    onDelete = {
                        transactionViewModel.deleteTransaction = it
                        transactionViewModel.showDeleteTransactionDialog = true
                    }
                )
            }

            item {

                if (transactionViewModel.showDeleteTransactionDialog) {
                    BasicAlertDialog(
                        onDismissRequest = {
                            transactionViewModel.showDeleteTransactionDialog = false
                        }
                    ) {
                        Card(
                            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black)
                        ) {
                            DeleteTransactionDialog(
                                transactionViewModel.deleteTransaction,
                                onConfirm = { id ->
                                    transactionViewModel.removeTransaction(id)
                                    transactionViewModel.showDeleteTransactionDialog = false
                                },
                                onDismiss = {
                                    transactionViewModel.showDeleteTransactionDialog = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}