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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asha.notezen.domain.model.Note

@Composable
fun SearchableNoteList(
    modifier: Modifier = Modifier,
    notes: List<Note>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onClick: (Note) -> Unit,
    onToggleArchive: (Note) -> Unit,
    onTogglePin: (Note) -> Unit,
    onDelete: (Note) -> Unit,
    showPinIcon: Boolean = true,
    showFAB: Boolean = true,
    onAddNote: (() -> Unit)? = null
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
                    items(notes, key = { it.id }) { note ->
                        SwipeToDeleteNoteCard(
                            note = note,
                            onClick = { onClick(note) },
                            onTogglePin = { if (showPinIcon) onTogglePin(note) },
                            onToggleArchive = { onToggleArchive(note) },
                            onDelete = { onDelete(note) },
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
