package com.asha.notezen.presentation.screens.addnote

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.presentation.ui.theme.noteColors

@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: AddNoteViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadNoteIfAvailable()
    }

    if (state.saveSuccess) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Color Bubbles
        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            noteColors.forEachIndexed { index, color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (state.selectedColorIndex == index) 3.dp else 1.dp,
                            color = if (state.selectedColorIndex == index) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                        .clickable { viewModel.onColorSelected(index) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Title Field
        OutlinedTextField(
            value = state.title,
            onValueChange = viewModel::onTitleChange,
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Content Field
        OutlinedTextField(
            value = state.content,
            onValueChange = viewModel::onContentChanged,
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Save Button
        Button(
            onClick = { viewModel.saveNote() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Save")
        }
    }
}