package com.asha.notezen.presentation.screens.notelist.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FABColumn(
    showScrollToTop: Boolean,
    onScrollToTop: () -> Unit,
    onAddNote: () -> Unit
) {
    Column(
        modifier = Modifier.padding(end = 16.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.End
    ) {
        if (showScrollToTop) {
            FloatingActionButton(
                onClick = onScrollToTop,
                shape = CircleShape,
                containerColor = Color.White,
                contentColor = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to Top")
            }
        }

        FloatingActionButton(
            onClick = onAddNote,
            shape = CircleShape,
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Note")
        }
    }
}
