package com.asha.notezen.presentation.screens.notelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.presentation.navigation.Screen
import com.asha.notezen.presentation.screens.notelist.composables.EmptyStateMessage
import com.asha.notezen.presentation.screens.notelist.composables.FABColumn
import com.asha.notezen.presentation.screens.notelist.composables.NoteCard
import com.asha.notezen.presentation.screens.notelist.composables.NotesTopBar
import com.asha.notezen.presentation.screens.notelist.composables.SearchBar
import com.asha.notezen.presentation.screens.notelist.composables.SortSection
import kotlinx.coroutines.launch


@OptIn(ExperimentalFoundationApi::class)
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

    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 3 }
    }

    LaunchedEffect(uiState.notes.size) {
        viewModel.hideSortSection()
        if (uiState.notes.isNotEmpty() && uiState.notes.size > previousSize.intValue) {
            listState.animateScrollToItem(0)
        }
        previousSize.intValue = uiState.notes.size
    }

    Scaffold(
        floatingActionButton = {
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
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            NotesTopBar(
                isSortSectionVisible = uiState.isSortSectionVisible,
                onToggleSort = { viewModel.toggleSortSection() }
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

            if (uiState.notes.isEmpty()) {
                EmptyStateMessage()
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                ) {
                    items(uiState.notes) { note ->
                        NoteCard(
                            note = note,
                            onDelete = { viewModel.deleteNote(note) },
                            onClick = {
                                navController.navigate(Screen.AddNote.passNoteId(note.id))
                            },
                            onTogglePin = { viewModel.togglePin(it) }
                        )
                    }
                }
            }

        }
    }
}

