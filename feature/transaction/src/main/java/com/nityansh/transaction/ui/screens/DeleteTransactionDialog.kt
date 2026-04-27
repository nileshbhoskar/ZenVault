package com.nityansh.transaction.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nityansh.domain.models.TransactionItem

@Composable
fun DeleteTransactionDialog(
    item: TransactionItem?,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    if (item == null) return
    DeleteTransactionDialogContent(
        item = item,
        onConfirm = onConfirm,
        onDismiss = { onDismiss() }
    )
}

@Composable
fun DeleteTransactionDialogContent(
    item: TransactionItem,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "Are you sure you want to delete this transaction?", color = Color.Black,
            textAlign = TextAlign.Center)

        Text(
            text = "Description: ${ item.description }",
            color = Color.Black,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(8.dp)
        )

        Text(
            text = "Amount: ${item.amount}",
            color = Color.Black,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(8.dp)
        )

        Text(
            text = "Date: ${item.date}",
            color = Color.Black,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(8.dp)
        )

        Row(modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,

        ) {
            OutlinedButton(
                onClick = { onConfirm(item.id) },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Yes", color = Color.Black)
            }

            OutlinedButton(
                onClick = onDismiss,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "No", color = Color.Black)
            }
        }
    }
}