package com.asha.notezen.presentation.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import com.asha.notezen.domain.model.Note
import com.asha.notezen.presentation.screens.notelist.composables.NoteCard
import com.asha.notezen.presentation.screens.notelist.composables.SearchBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchableNoteList(
    notes: List<Note>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClick: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    onToggleArchive: (Note) -> Unit,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column (modifier = modifier) {
        SearchBar(
            value = searchQuery,
            onValueChange = {
                onSearchQueryChange(it)
                if (it.isBlank()) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            }
        )

        if (notes.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No notes found")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(notes) { note ->
                    NoteCard(
                        note = note,
                        onClick = { onClick(note) },
                        onDelete = { onDelete(note) },
                        onTogglePin = {}, // archived screen doesn't show pin
                        onToggleArchive = { onToggleArchive(note) }
                    )
                }
            }
        }
    }
}
