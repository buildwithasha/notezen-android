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
import androidx.compose.ui.graphics.toArgb
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

        val colors = listOf(
            Color(0xFFFFCDD2), // red
            Color(0xFFFFF9C4), // yellow
            Color(0xFFC8E6C9), // green
            Color(0xFFBBDEFB), // blue
            Color(0xFFD1C4E9)  // purple
        )

        Row(modifier = Modifier.padding(vertical = 12.dp)) {
            colors.forEach { color ->
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                        .border(
                            width = if (viewModel.selectedColor == color.toArgb()) 3.dp else 1.dp,
                            color = if (viewModel.selectedColor == color.toArgb()) Color.Black else Color.Gray,
                            shape = CircleShape
                        )
                        .clickable {
                            viewModel.onColorSelected(color.toArgb())
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

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