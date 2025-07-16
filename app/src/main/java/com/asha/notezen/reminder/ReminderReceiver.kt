package com.asha.notezen.reminder

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.asha.notezen.MainActivity
import com.asha.notezen.R
import android.app.PendingIntent
import android.util.Log
import androidx.annotation.RequiresPermission
import com.asha.notezen.utils.ReminderConstants.EXTRA_NOTE_CONTENT
import com.asha.notezen.utils.ReminderConstants.EXTRA_NOTE_ID
import com.asha.notezen.utils.ReminderConstants.EXTRA_NOTE_TITLE

class ReminderReceiver : BroadcastReceiver() {
    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ReminderReceiver", "Alarm received for noteId: ${intent.getIntExtra("noteId", -1)}")

        val noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1)
        val title = intent.getStringExtra(EXTRA_NOTE_TITLE) ?: "Reminder"
        val content = intent.getStringExtra(EXTRA_NOTE_CONTENT) ?: ""

        val openIntent = Intent(context, MainActivity::class.java).apply {
            putExtra("noteId", noteId)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            noteId,
            openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, "reminder_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(noteId, notification)
    }
}
