package com.asha.notezen.presentation.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.asha.notezen.domain.model.Note
import com.asha.notezen.presentation.screens.notelist.composables.NoteCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteNoteCard(
    note: Note,
    onDelete: (Note) -> Unit,
    onClick: () -> Unit,
    onTogglePin: (Note) -> Unit,
    onToggleArchive: (Note) -> Unit,
    modifier: Modifier = Modifier,
    showPinIcon: Boolean = true,
    cornerRadius: Dp = 12.dp
) {
    val dismissState = rememberDismissState(
        confirmValueChange = { value ->
            if (value == DismissValue.DismissedToStart) {
                onDelete(note)
                true
            } else false
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        background = {
            val targetColor = if (dismissState.targetValue == DismissValue.DismissedToStart)
                Color(0xFF4CAF50)
            else
                Color(0xFF4CAF50).copy(alpha = 0.8f)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
                    .background(
                        color = targetColor,
                        shape = RoundedCornerShape(cornerRadius)
                    )
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White
                )
            }
        },
        dismissContent = {
            NoteCard(
                note = note,
                onClick = onClick,
                onTogglePin = onTogglePin,
                onToggleArchive = onToggleArchive,
                modifier = modifier,
                cornerRadius = cornerRadius,
                showPinIcon = showPinIcon
            )
        }
    )
}
