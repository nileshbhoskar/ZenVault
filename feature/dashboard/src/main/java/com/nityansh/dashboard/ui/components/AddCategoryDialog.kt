package com.nityansh.dashboard.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nityansh.dashboard.viewmodel.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDialog(dashboardViewModel: DashboardViewModel) {

    BasicAlertDialog(
        onDismissRequest = {
            dashboardViewModel.showAddCategoryDialog = false
        }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            AddCategoryContent(dashboardViewModel = dashboardViewModel)
        }
    }
}

@Composable
fun AddCategoryContent(dashboardViewModel: DashboardViewModel) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .testTag("Add New Category Dialog")
        .padding(16.dp)) {
        Text(
            text = "Add New Category",
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Add New Category Dialog Title"
                },
            textAlign = TextAlign.Center
        )

        OutlinedTextField(
            modifier = Modifier.padding(bottom = 4.dp).semantics{
                contentDescription = "Category Name Input Field"
            },
            value = dashboardViewModel.newCategoryName,
            onValueChange = {
                dashboardViewModel.newCategoryName = it
                dashboardViewModel.categoryNameError = false
            },
            label = { Text("Category Name") })
        if (dashboardViewModel.categoryNameError) {
            Text(
                text = "Enter valid category name",
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        Text(
            text = "Status:", modifier = Modifier
                .padding(bottom = 8.dp, top = 16.dp)
                .semantics {
                    contentDescription = "Title Status for Category Status Options"
                })

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(end = 8.dp)
                    .semantics {
                        contentDescription = "Enabled Option"
                        role = Role.RadioButton
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = dashboardViewModel.selectedOption == "Enabled",
                    onClick = { dashboardViewModel.selectedOption = "Enabled" },
                )
                Text(text = "Enabled", modifier = Modifier.padding(start = 8.dp), maxLines = 1)
            }

            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(end = 8.dp)
                    .semantics {
                        contentDescription = "Disabled Option"
                        role = Role.RadioButton
                    },
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = dashboardViewModel.selectedOption == "Disabled",
                    onClick = { dashboardViewModel.selectedOption = "Disabled" })
                Text(text = "Disabled", modifier = Modifier.padding(start = 8.dp), maxLines = 1)
            }
        }

        if (dashboardViewModel.categoryStatusError) {
            Text(
                text = "Select category status",
                color = Color.Red,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = {
                    dashboardViewModel.addCategory()
                }, modifier = Modifier
                    .padding(top = 16.dp)
                    .semantics{
                        contentDescription = "Add Buttons"
                        role = Role.Button
                    }) {
                Text(text = "Add", color = Color.Black)
            }

            OutlinedButton(
                onClick = {
                    dashboardViewModel.addCategoryDialogClosed()
                }, modifier = Modifier
                    .padding(top = 16.dp)
                    .semantics {
                        contentDescription = "Cancel Buttons"
                        role = Role.Button
                    }) {
                Text(text = "Cancel", color = Color.Black)
            }
        }
    }
}