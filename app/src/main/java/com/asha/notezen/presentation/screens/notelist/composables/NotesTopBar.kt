package com.asha.notezen.presentation.screens.notelist.composables


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun NotesTopBar(
    isSortSectionVisible: Boolean,
    onToggleSort: () -> Unit
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isSortSectionVisible) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "SortIconRotation"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Your Notes",
            style = MaterialTheme.typography.headlineSmall.copy(
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
        )

        IconButton(onClick = onToggleSort) {
            Icon(
                imageVector = Icons.Default.Sort,
                contentDescription = "Sort Notes",
                modifier = Modifier.rotate(rotationAngle),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}
