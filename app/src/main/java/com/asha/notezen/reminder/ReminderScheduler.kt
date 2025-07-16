package com.asha.notezen.reminder

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.annotation.RequiresPermission
import com.asha.notezen.domain.model.Note
import com.asha.notezen.utils.ReminderConstants.EXTRA_NOTE_CONTENT
import com.asha.notezen.utils.ReminderConstants.EXTRA_NOTE_ID
import com.asha.notezen.utils.ReminderConstants.EXTRA_NOTE_TITLE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @RequiresPermission(Manifest.permission.SCHEDULE_EXACT_ALARM)
    fun schedule(note: Note) {
        if (note.reminderTime == null) return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(EXTRA_NOTE_ID, note.id)
            putExtra(EXTRA_NOTE_TITLE, note.title)
            putExtra(EXTRA_NOTE_CONTENT, note.content)

        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            note.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                note.reminderTime,
                pendingIntent
            )
        } catch (e: SecurityException) {
            throw e
        }
    }

    fun cancel(noteId: Int) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            noteId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(AlarmManager::class.java)
        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }
}
