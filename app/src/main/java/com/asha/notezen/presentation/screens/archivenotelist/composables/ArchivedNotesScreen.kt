package com.asha.notezen.presentation.screens.archivenotelist.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.domain.model.Note
import com.asha.notezen.presentation.navigation.Screen
import com.asha.notezen.presentation.screens.archivenotelist.ArchivedNotesListViewModel
import com.asha.notezen.presentation.screens.common.SearchableNoteList
import com.asha.notezen.presentation.screens.common.UndoDeleteSnackbarEffect
import com.asha.notezen.presentation.screens.composables.CommonTopBar
import com.asha.notezen.presentation.screens.notelist.composables.SearchBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ArchivedNotesScreen(
    navController: NavController,
    viewModel: ArchivedNotesListViewModel = hiltViewModel()
) {

    val filteredNotes by viewModel.filteredNotes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val snackbarHostState = remember { SnackbarHostState() }
    val recentlyDeletedNote by viewModel.recentlyDeletedNote.collectAsState()

    val onNoteClick: (Note) -> Unit = {
        navController.navigate(Screen.AddNote.passNoteId(it.id))
    }

    UndoDeleteSnackbarEffect(
        snackbarHostState = snackbarHostState,
        recentlyDeletedNote = recentlyDeletedNote,
        onUndo = { viewModel.restoreDeletedNote() },
        onDismiss = { viewModel.clearRecentlyDeletedNote() }
    )

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CommonTopBar(
                title = "Archived Notes",
                showBackIcon = true,
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    start = 16.dp,
                    end = 16.dp,
                    bottom = innerPadding.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            SearchBar(
                value = searchQuery,
                onValueChange = {
                    viewModel.onSearchQueryChanged(it)
                    if (it.isBlank()) {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            SearchableNoteList(
                notes = filteredNotes,
                searchQuery = searchQuery,
                onSearchQueryChange = viewModel::onSearchQueryChanged,
                onClick = onNoteClick,
                onToggleArchive = viewModel::toggleArchive,
                onTogglePin = {},
                showFAB = false,
                showPinIcon = false,
                onAddNote = null,
                onDelete = { viewModel.deleteNote(it) }
            )

        }
    }
}