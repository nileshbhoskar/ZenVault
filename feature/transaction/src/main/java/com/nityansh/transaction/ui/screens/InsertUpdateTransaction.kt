package com.nityansh.transaction.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nityansh.transaction.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateInsertTransactionScreen(
    viewModel: TransactionViewModel = hiltViewModel(), onAddOrUpdateSuccess: () -> Unit
) {

    var showDatePicker by remember { mutableStateOf(false) }
    val selectedDateState = rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Header(title = if (viewModel.transactionId == -1) "Add Transaction" else "Edit Transaction")

        CategorySelector(viewModel)

        OutlinedTextField(
            value = viewModel.description,
            onValueChange = { viewModel.description = it },
            label = { Text("Description") },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            singleLine = true,
            isError = viewModel.descriptionError
        )
        if (viewModel.descriptionError) {
            Text(text = "Enter valid description", color = MaterialTheme.colorScheme.error)
        }

        OutlinedTextField(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            value = viewModel.transactionDate,
            onValueChange = {},
            label = { Text("Date") },
            placeholder = { Text("Select Date") },
            readOnly = true,
            singleLine = true,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Empty",
                    modifier = Modifier.clickable(
                        onClick = {
                            Log.d("DatePickerTextField", "Clicked")
                            showDatePicker = true
                        }))
            },
            isError = viewModel.dateError,
        )
        if (viewModel.dateError) {
            Text(text = "Enter valid date", color = MaterialTheme.colorScheme.error)
        }

        if (showDatePicker) {

            androidx.compose.material3.DatePickerDialog(onDismissRequest = {
                showDatePicker = false
            }, confirmButton = {
                TextButton(onClick = {
                    selectedDateState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                        viewModel.transactionDate = sdf.format(millis)
                        viewModel.transactionDateInMilli = millis
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }, dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }) {
                DatePicker(state = selectedDateState)
            }
        }

        OutlinedTextField(
            value = viewModel.amount,
            onValueChange = { viewModel.amount = it },
            label = { Text("Amount") },
            placeholder = { Text("0.0") },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            singleLine = true,
            isError = viewModel.amountError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (viewModel.amountError) {
            Text(text = "Enter valid amount", color = MaterialTheme.colorScheme.error)
        }

        OutlinedButton(
            onClick = {
                viewModel.storeTransaction()
                onAddOrUpdateSuccess()
            },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Text(text = if (viewModel.transactionId == -1) "Add Transaction" else "Update Transaction")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(viewModel: TransactionViewModel) {

    val categories by viewModel.categories.collectAsState()

    ExposedDropdownMenuBox(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        expanded = viewModel.categoryDropDownExpanded,
        onExpandedChange = { /*viewModel.expanded = !viewModel.expanded*/ }) {
        OutlinedTextField(
            value = viewModel.selectedCategoryName.orEmpty(),
            onValueChange = { },
            readOnly = true,
            label = { Text("Select Category") },
            placeholder = { Text("Select Category") },
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth()
                .wrapContentHeight(),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = viewModel.categoryDropDownExpanded,
                    modifier = Modifier.clickable(
                        enabled = true, onClick = {
                            viewModel.categoryDropDownExpanded = !viewModel.categoryDropDownExpanded
                        }))
            },
            singleLine = true,
//            isError = viewModel.amountError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        ExposedDropdownMenu(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp),
            expanded = viewModel.categoryDropDownExpanded,
            onDismissRequest = { viewModel.categoryDropDownExpanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp),
                    text = { Text(category.name) },
                    onClick = {
                        viewModel.selectedCategory = category
                        viewModel.selectedCategoryName = category.name
                        viewModel.categoryDropDownExpanded = false
                    })
            }
        }
    }
}