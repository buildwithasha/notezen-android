package com.asha.notezen.presentation.screens.addnote.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun ChecklistAddField(
    onAdd: (String) -> Unit
) {
    var newItemText by remember { mutableStateOf("") }

    Row (
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = newItemText,
            onValueChange = { newItemText = it },
            placeholder = { Text("Add checklist item") },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
            ),
            modifier = Modifier.weight(1f)
        )
        IconButton(
            onClick = {
                if (newItemText.isNotBlank()) {
                    onAdd(newItemText)
                    newItemText = ""
                }
            }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}
