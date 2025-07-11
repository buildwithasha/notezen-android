package com.asha.notezen.presentation.screens.addnote.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asha.notezen.utils.formatDate

@Composable
fun ReminderChip(
    reminderTime: Long?,
    onPickReminder: () -> Unit,
    onClearReminder: () -> Unit,
    modifier: Modifier = Modifier
) {
    val chipShape = RoundedCornerShape(24.dp)
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.Black.copy(alpha = 0.05f),
            shape = chipShape,
            modifier = Modifier
                .clip(chipShape) // clip to rounded shape
                .clickable(
                    interactionSource = interactionSource,
                    indication = rememberRipple(
                        bounded = true,
                        radius = 200.dp,
                        color = Color.Black.copy(alpha = 0.15f)
                    ),
                    onClick = onPickReminder
                )
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Reminder",
                    tint = Color.Black,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = reminderTime?.let { formatDate(it) } ?: "Add reminder",
                    color = Color.Black,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )

                if (reminderTime != null) {
                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = rememberRipple(
                                    bounded = false,
                                    radius = 16.dp,
                                    color = Color.Black.copy(alpha = 0.2f)
                                ),
                                onClick = onClearReminder
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = "Clear Reminder",
                            tint = Color.Black,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
