package com.asha.notezen.presentation.screens.notelist.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asha.notezen.domain.model.Note
import com.asha.notezen.presentation.screens.common.SwipeToDeleteNoteCard

fun LazyListScope.NoteSection(
    title: String,
    notes: List<Note>,
    onClick: (Note) -> Unit,
    onTogglePin: (Note) -> Unit,
    onArchive: (Note) -> Unit,
    onDelete: (Note) -> Unit
) {
    if (notes.isNotEmpty()) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        items(notes, key = { it.id }) { note ->
            SwipeToDeleteNoteCard(
                note = note,
                onClick = { onClick(note) },
                onTogglePin = { onTogglePin(note) },
                onToggleArchive = { onArchive(note) },
                onDelete = { onDelete(note) },
                showPinIcon = true
            )
        }
    }
}
