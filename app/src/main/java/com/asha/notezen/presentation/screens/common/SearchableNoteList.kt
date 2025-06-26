package com.asha.notezen.presentation.screens.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asha.notezen.domain.model.Note
import com.asha.notezen.presentation.screens.notelist.composables.NoteCard

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchableNoteList(
    notes: List<Note>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClick: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    onToggleArchive: (Note) -> Unit,
    onTogglePin: (Note) -> Unit,
    showPinIcon: Boolean = true,
    showFAB: Boolean = true,
    onAddNote: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {

    Box(modifier = modifier.fillMaxSize()) {
            if (notes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 48.dp),
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
                            onTogglePin = { if (showPinIcon) onTogglePin(note) }, // archived screen doesn't show pin
                            onToggleArchive = { onToggleArchive(note) },
                            showPinIcon = showPinIcon
                        )
                    }
                }
            }
        if (showFAB && onAddNote != null) {
            FloatingActionButton(
                onClick = onAddNote,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Note"
                )
            }
        }

    }
}
