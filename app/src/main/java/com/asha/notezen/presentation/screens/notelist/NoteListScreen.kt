package com.asha.notezen.presentation.screens.notelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.core.graphics.toColorInt

@Composable
fun NoteListScreen(navController: NavController, viewModel: NoteListViewModel = hiltViewModel()) {

    val noteList by viewModel.state.collectAsState()

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = { navController.navigate("add_note") }) {
            Icon(Icons.Default.Add, contentDescription = "Add Note")
        }
    }) {
        LazyColumn(modifier = Modifier.padding(it)) {
            items(noteList) { note ->

                val noteColor = Color(note.colorHex.toColorInt())

                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable { },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = noteColor)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = note.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )
                        if (note.content.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = note.content,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }

                    }

                }

            }
        }
    }
}