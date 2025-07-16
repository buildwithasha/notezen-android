    package com.asha.notezen.presentation.screens.addnote.composables

    import androidx.compose.material3.AlertDialog
    import androidx.compose.material3.Text
    import androidx.compose.material3.TextButton
    import androidx.compose.runtime.Composable

    @Composable
    fun ExactAlarmPermissionDialog(
        onDismiss: () -> Unit,
        onOpenSettings: () -> Unit,
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text("Allow Exact Alarms") },
            text = {
                Text(
                    "To receive reminders exactly on time, please allow the app to schedule exact alarms. You can enable this in your device's settings."
                )
            },
            confirmButton = {
                TextButton(onClick = onOpenSettings) {
                    Text("Go to Settings")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Maybe Later")
                }
            }
        )
    }
