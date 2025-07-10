package com.asha.notezen.presentation.screens.addnote.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asha.notezen.domain.model.NoteType

@Composable
fun NoteTypeToggle(
    selectedType: NoteType,
    onTypeSelected: (NoteType) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        val buttonModifier = Modifier
            .weight(1f)
            .padding(horizontal = 4.dp)

        val selectedColor = MaterialTheme.colorScheme.onSurface
        val unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)

        val textStyle = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)

        TextButton(
            onClick = { onTypeSelected(NoteType.TEXT) },
            modifier = buttonModifier,
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (selectedType == NoteType.TEXT) selectedColor else unselectedColor
            )
        ) {
            Text(
                text = "Text Note",
                fontWeight = if (selectedType == NoteType.TEXT) FontWeight.Bold else FontWeight.Normal,
                style = textStyle
            )
        }

        TextButton(
            onClick = { onTypeSelected(NoteType.CHECKLIST) },
            modifier = buttonModifier,
            colors = ButtonDefaults.textButtonColors(
                contentColor = if (selectedType == NoteType.CHECKLIST) selectedColor else unselectedColor
            )
        ) {
            Text(
                text = "Checklist Note",
                fontWeight = if (selectedType == NoteType.CHECKLIST) FontWeight.Bold else FontWeight.Normal,
                style = textStyle
            )
        }
    }
}
