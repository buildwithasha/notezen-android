package com.asha.notezen.presentation.screens.notelist

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.domain.model.Note
import com.asha.notezen.presentation.navigation.Screen
import com.asha.notezen.presentation.screens.common.SearchableNoteList
import com.asha.notezen.presentation.screens.common.UndoDeleteSnackbarEffect
import com.asha.notezen.presentation.screens.composables.CommonTopBar
import com.asha.notezen.presentation.screens.notelist.composables.EmptyStateMessage
import com.asha.notezen.presentation.screens.notelist.composables.FABColumn
import com.asha.notezen.presentation.screens.notelist.composables.NoteSection
import com.asha.notezen.presentation.screens.notelist.composables.SearchBar
import com.asha.notezen.presentation.screens.notelist.composables.SortSection
import kotlinx.coroutines.launch

@Composable
fun NoteListScreen(
    navController: NavController,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val listState = rememberLazyListState()
    val previousSize = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    val pinnedNotes = uiState.notes.filter { it.isPinned }
    val otherNotes = uiState.notes.filter { !it.isPinned }

    val snackbarHostState = remember { SnackbarHostState() }
    val recentlyDeletedNote by viewModel.recentlyDeletedNote.collectAsState()

    UndoDeleteSnackbarEffect(
        snackbarHostState = snackbarHostState,
        recentlyDeletedNote = recentlyDeletedNote,
        onUndo = { viewModel.restoreDeletedNote() },
        onDismiss = { viewModel.clearRecentlyDeletedNote() }
    )


    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 3 }
    }

    val isSearching by remember {
        derivedStateOf { searchQuery.isNotBlank() }
    }

    LaunchedEffect(uiState.notes.size) {
        viewModel.hideSortSection()
        if (uiState.notes.isNotEmpty() && uiState.notes.size > previousSize.intValue) {
            listState.animateScrollToItem(0)
        }
        previousSize.intValue = uiState.notes.size
    }


    val focusManager = LocalFocusManager.current

    val onNoteClick: (Note) -> Unit = { note ->
        navController.navigate(Screen.AddNote.passNoteId(note.id))
    }

    BackHandler(enabled = searchQuery.isNotBlank()) {
        viewModel.onSearchQueryChanged("")
        focusManager.clearFocus(force = true)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        floatingActionButton = {
            if (!isSearching) {
                FABColumn(
                    showScrollToTop = showScrollToTop,
                    onScrollToTop = {
                        scope.launch { listState.animateScrollToItem(0) }
                    },
                    onAddNote = {
                        navController.navigate(Screen.AddNote.route)
                    }
                )
            }
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            CommonTopBar(
                title = "Your Notes",
                showSortIcon = true,
                isSortSectionVisible = uiState.isSortSectionVisible,
                onToggleSort = { viewModel.toggleSortSection() },
                showArchiveIcon = true,
                onArchiveClick = { navController.navigate(Screen.ArchivedNotes.route) }
            )

            AnimatedVisibility(
                visible = uiState.isSortSectionVisible,
                enter = fadeIn(animationSpec = tween(200)) +
                        expandVertically(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(200)) +
                        shrinkVertically(animationSpec = tween(250))
            ) {
                SortSection(
                    sortType = uiState.sortType,
                    sortOrder = uiState.sortOrder,
                    onSortTypeChange = { viewModel.updateSort(type = it) },
                    onSortOrderChange = { viewModel.updateSort(order = it) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            SearchBar(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChanged
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (isSearching) {
                SearchableNoteList(
                    notes = uiState.notes,
                    searchQuery = searchQuery,
                    onSearchQueryChange = viewModel::onSearchQueryChanged,
                    onClick = onNoteClick,
                    onToggleArchive = viewModel::archiveNote,
                    onTogglePin = viewModel::togglePin,
                    showFAB = false,
                    showPinIcon = true,
                    onAddNote = null,
                    onDelete = { viewModel.deleteNote(it) }
                )
            } else {
                if (uiState.notes.isEmpty()) {
                    EmptyStateMessage()
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        NoteSection(
                            title = "Pinned",
                            notes = pinnedNotes,
                            onClick = onNoteClick,
                            onTogglePin = viewModel::togglePin,
                            onArchive = viewModel::archiveNote,
                            onDelete = { viewModel.deleteNote(it) }
                        )

                        NoteSection(
                            title = "Others",
                            notes = otherNotes,
                            onClick = onNoteClick,
                            onTogglePin = viewModel::togglePin,
                            onArchive = viewModel::archiveNote,
                            onDelete = { viewModel.deleteNote(it) }
                        )
                    }

                }
            }
        }
    }
}