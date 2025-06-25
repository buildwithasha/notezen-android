package com.asha.notezen.presentation.screens.archivenotelist.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.presentation.navigation.Screen
import com.asha.notezen.presentation.screens.archivenotelist.ArchivedNotesListViewModel
import com.asha.notezen.presentation.screens.composables.CommonTopBar
import com.asha.notezen.presentation.screens.notelist.composables.NoteCard

@Composable
fun ArchivedNotesScreen(
    navController: NavController,
    viewModel: ArchivedNotesListViewModel = hiltViewModel()
) {
    val archivedNotes by viewModel.archivedNotes.collectAsState()

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Archived Notes",
                showBackIcon = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        if (archivedNotes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("No archived notes")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                items(archivedNotes) { note ->
                    NoteCard(
                        note = note,
                        onDelete = { viewModel.deleteNote(note) },
                        onClick = {
                            navController.navigate(Screen.AddNote.passNoteId(note.id))
                        },
                        onTogglePin = {},
                        onToggleArchive = { viewModel.toggleArchive(note) },
                        showPinIcon = false
                    )
                }
            }
        }
    }
}