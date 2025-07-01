package com.asha.notezen.presentation.screens.common

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.asha.notezen.domain.model.Note

@Composable
fun UndoDeleteSnackbarEffect(
    snackbarHostState: SnackbarHostState,
    recentlyDeletedNote: Note?,
    onUndo: () -> Unit,
    onDismiss: () -> Unit
) {
    LaunchedEffect(recentlyDeletedNote) {
        recentlyDeletedNote?.let {
            val result = snackbarHostState.showSnackbar(
                message = "Note deleted",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                onUndo()
            }
            onDismiss()
        }
    }
}
