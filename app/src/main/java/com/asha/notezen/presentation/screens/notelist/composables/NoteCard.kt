package com.asha.notezen.presentation.screens.notelist.composables


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.outlined.PushPin
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import com.asha.notezen.domain.model.Note
import com.asha.notezen.presentation.screens.notelist.formatDate

@Composable
fun NoteCard(
    note: Note,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    onTogglePin: (Note) -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 12.dp
) {
    val backgroundColor = Color(note.colorHex.toColorInt())
    val textColor = if (backgroundColor.luminance() < 0.5f) Color.White else Color.Black


    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(cornerRadius),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = backgroundColor)
        ) {
            Box {
                Column(modifier = Modifier.padding(16.dp)) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = note.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor,
                            modifier = Modifier.weight(1f)
                        )

                        val pinIcon =
                            if (note.isPinned) Icons.Default.PushPin else Icons.Outlined.PushPin

                        Icon(
                            imageVector = pinIcon,
                            contentDescription = if (note.isPinned) "Unpin Note" else "Pin Note",
                            tint = textColor.copy(alpha = 0.8f),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onTogglePin(note) }
                        )
                    }

                    if (note.content.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = note.content,
                            fontSize = 14.sp,
                            color = textColor.copy(alpha = 0.95f),
                            maxLines = 6
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = formatDate(note.timestamp),
                            fontSize = 12.sp,
                            color = textColor.copy(alpha = 0.7f),
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Note",
                            tint = textColor.copy(alpha = 0.8f),
                            modifier = Modifier
                                .size(20.dp)
                                .clickable { onDelete() }
                        )
                    }
                }
            }
        }
    }
}