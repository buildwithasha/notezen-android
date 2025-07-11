package com.asha.notezen.presentation.screens.addnote

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.domain.model.NoteType
import com.asha.notezen.presentation.screens.addnote.composables.ChecklistAddField
import com.asha.notezen.presentation.screens.addnote.composables.ColorPickerRow
import com.asha.notezen.presentation.screens.addnote.composables.EditableChecklistItem
import com.asha.notezen.presentation.screens.addnote.composables.ExactAlarmPermissionDialog
import com.asha.notezen.presentation.screens.addnote.composables.NoteTypeToggle
import com.asha.notezen.presentation.screens.addnote.composables.ReminderBottomSheetContent
import com.asha.notezen.presentation.screens.addnote.composables.ReminderChip
import com.asha.notezen.presentation.ui.theme.noteColors
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: AddNoteViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadNoteIfAvailable()
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val context = LocalContext.current
        val permissionLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (!isGranted) {
                    Toast.makeText(
                        context,
                        "Notification permission is required to get reminders.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        )

        LaunchedEffect(Unit) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }


    if (state.saveSuccess) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = noteColors[state.selectedColorIndex],
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveNote() },
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Note")
            }
        }
    )
     { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(noteColors[state.selectedColorIndex])
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp)
        ) {
            ColorPickerRow(
                noteColors = noteColors,
                selectedIndex = state.selectedColorIndex,
                onColorSelected = viewModel::onColorSelected
            )

            Spacer(modifier = Modifier.height(12.dp))

            NoteTypeToggle(
                selectedType = state.noteType,
                onTypeSelected = viewModel::setNoteType
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                ReminderChip(
                    reminderTime = state.reminderTime,
                    onPickReminder = { viewModel.showReminderSheet(true) },
                    onClearReminder = { viewModel.setReminderTime(null) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = state.title,
                onValueChange = viewModel::onTitleChanged,
                placeholder = {
                    Text("Title", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                when (state.noteType) {
                    NoteType.TEXT -> {
                        item {
                            TextField(
                                value = state.content,
                                onValueChange = viewModel::onContentChanged,
                                placeholder = { Text("Note content...", fontSize = 16.sp) },
                                textStyle = TextStyle(fontSize = 16.sp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    NoteType.CHECKLIST -> {
                        itemsIndexed(viewModel.checklistItems) { index, item ->
                            EditableChecklistItem(
                                item = item,
                                index = index,
                                onToggle = viewModel::toggleChecklistItem,
                                onRemove = viewModel::removeChecklistItem,
                                onTextChange = viewModel::updateChecklistItemText
                            )
                        }

                        item {
                            ChecklistAddField(
                                onAdd = viewModel::addChecklistItem
                            )
                        }
                    }
                }
            }

            if (state.isReminderSheetVisible) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                ModalBottomSheet(
                    onDismissRequest = { viewModel.showReminderSheet(false) },
                    sheetState = sheetState
                ) {
                    ReminderBottomSheetContent (
                        onPickDateTime = { timestamp ->
                            viewModel.setReminderTime(timestamp)
                            viewModel.showReminderSheet(false)
                        },
                        onCancel = {
                            viewModel.showReminderSheet(false)
                        }
                    )
                }
            }

            if (viewModel.showExactAlarmPermissionDialog.value) {
                ExactAlarmPermissionDialog(
                    onDismiss = { viewModel.onExactAlarmPermissionDialogDismissed() },
                    onOpenSettings = {
                        viewModel.onOpenExactAlarmSettings()
                        viewModel.onExactAlarmPermissionDialogDismissed()
                    }
                )
            }
        }
    }
}

fun showDateTimePicker(context: Context, onTimeSelected: (Long) -> Unit) {

    val currentTime = System.currentTimeMillis()
    val calendar = Calendar.getInstance()
    DatePickerDialog(
        context,
        { _, year, month, day ->
            val date = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
            }

            TimePickerDialog(
                context,
                { _, hour, minute ->
                    date.set(Calendar.HOUR_OF_DAY, hour)
                    date.set(Calendar.MINUTE, minute)
                    date.set(Calendar.SECOND, 0)
                    if (date.timeInMillis > System.currentTimeMillis()) {
                        onTimeSelected(date.timeInMillis)
                    } else {
                        Toast.makeText(context, "Please select a future time", Toast.LENGTH_SHORT).show()
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        },

        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    ).apply {
        datePicker.minDate = currentTime
    }.show()
}
