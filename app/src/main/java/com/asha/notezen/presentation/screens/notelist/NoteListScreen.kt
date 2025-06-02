package com.asha.notezen.presentation.screens.notelist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(16.dp)
                )
                {
                    Text(text = note.title)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = note.content)

                }
            }
        }
    }
}