package com.asha.notezen.presentation.screens.archivenotelist.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.presentation.navigation.Screen
import com.asha.notezen.presentation.screens.archivenotelist.ArchivedNotesListViewModel
import com.asha.notezen.presentation.screens.common.SearchableNoteList
import com.asha.notezen.presentation.screens.composables.CommonTopBar

@Composable
fun ArchivedNotesScreen(
    navController: NavController,
    viewModel: ArchivedNotesListViewModel = hiltViewModel()
) {

    val filteredNotes by viewModel.filteredNotes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Scaffold(
        topBar = {
            CommonTopBar(
                title = "Archived Notes",
                showBackIcon = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        SearchableNoteList(
            notes = filteredNotes,
            searchQuery = searchQuery,
            onSearchQueryChange = viewModel::onSearchQueryChanged,
            onClick = { note -> navController.navigate(Screen.AddNote.passNoteId(note.id)) },
            onDelete = viewModel::deleteNote,
            onToggleArchive = viewModel::toggleArchive,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )

    }
}