package com.asha.notezen.presentation.screens.addnote.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.asha.notezen.presentation.screens.addnote.showDateTimePicker
import java.util.Calendar

@Composable
fun ReminderBottomSheetContent(
    onPickDateTime: (Long) -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val now = remember { Calendar.getInstance() }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Set Reminder", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)

        Spacer(modifier = Modifier.height(16.dp))

        TextButton (onClick = {
            val calendar = now.clone() as Calendar
            calendar.set(Calendar.HOUR_OF_DAY, 18)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            onPickDateTime(calendar.timeInMillis)
        }) {
            Text("Later today (6 PM)", color = Color.Black)
        }

        TextButton(onClick = {
            val calendar = now.clone() as Calendar
            calendar.add(Calendar.DATE, 1)
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            onPickDateTime(calendar.timeInMillis)
        }) {
            Text("Tomorrow morning (8 AM)", color = Color.Black)
        }

        TextButton(onClick = {
            val calendar = now.clone() as Calendar
            calendar.add(Calendar.DAY_OF_WEEK, (Calendar.MONDAY - now.get(Calendar.DAY_OF_WEEK) + 7) % 7)
            calendar.set(Calendar.HOUR_OF_DAY, 9)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            onPickDateTime(calendar.timeInMillis)
        }) {
            Text("Next week (Monday 9 AM)", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            showDateTimePicker(context) { millis ->
                onPickDateTime(millis)
            }
        }) {
            Text("Pick date & time", color = Color.Black)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onCancel) {
            Text("Cancel", color = Color.Black)
        }
    }
}
