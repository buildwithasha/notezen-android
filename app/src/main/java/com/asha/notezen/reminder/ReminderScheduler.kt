package com.asha.notezen.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.asha.notezen.domain.model.Note
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun schedule(note: Note) {
        val reminderTime = note.reminderTime ?: return
        if (reminderTime <= System.currentTimeMillis()) return // no past alarms

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("noteId", note.id)
            putExtra("noteTitle", note.title)
            putExtra("noteContent", note.content)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            note.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminderTime,
                        pendingIntent
                    )
                }

                else -> {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        reminderTime,
                        pendingIntent
                    )
                }
            }
        } catch (securityException: SecurityException) {
            throw securityException
        }
    }

    fun cancel(noteId: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            noteId,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }
}
