package com.asha.notezen.presentation.screens.composables


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTopBar(
    title: String,
    showSortIcon: Boolean = false,
    onToggleSort: (() -> Unit)? = null,
    showArchiveIcon: Boolean = false,
    onArchiveClick: (() -> Unit)? = null,
    showBackIcon: Boolean = false,
    onBackClick: (() -> Unit)? = null
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (showSortIcon && onToggleSort != null) 180f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "SortIconRotation"
    )

    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            if (showBackIcon && onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (showArchiveIcon && onArchiveClick != null) {
                IconButton(onClick = onArchiveClick) {
                    Icon(
                        imageVector = Icons.Default.Archive,
                        contentDescription = "View Archived Notes"
                    )
                }
            }

            if (showSortIcon && onToggleSort != null) {
                val rotationAngle by animateFloatAsState(
                    targetValue = 180f,
                    animationSpec = tween(durationMillis = 300),
                    label = "SortIconRotation"
                )

                IconButton(onClick = onToggleSort) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort Notes",
                        modifier = Modifier.rotate(rotationAngle)
                    )
                }
            }
        }
    )
}