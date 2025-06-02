package com.asha.notezen.presentation.screens.addnote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AddNoteScreen(navController: NavController, viewModel: AddNoteViewModel = hiltViewModel()) {

    val title = viewModel.title
    val content = viewModel.content
    val saveSuccess = viewModel.saveSuccess

    if (saveSuccess) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(value = title, onValueChange = viewModel::onTitleChange, label = {
            Text(
                "Title"
            )
        }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(value = content, onValueChange = viewModel::onContentChanged, label = {
            Text(text = "Content")
        }, modifier = Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = { viewModel.saveNote() }, modifier = Modifier.align(Alignment.End)) {
            Text("Save")
        }
    }
}