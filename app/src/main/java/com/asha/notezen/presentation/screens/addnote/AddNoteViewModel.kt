package com.asha.notezen.presentation.screens.addnote

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asha.notezen.domain.model.ChecklistItem
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.model.NoteType
import com.asha.notezen.domain.usecase.NoteUseCases
import com.asha.notezen.presentation.ui.theme.noteColors
import com.asha.notezen.reminder.ReminderScheduler
import com.asha.notezen.reminder.canScheduleExactAlarms
import com.asha.notezen.reminder.openExactAlarmPermissionSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle,
    private val reminderScheduler: ReminderScheduler,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _showExactAlarmPermissionDialog = mutableStateOf(false)
    val showExactAlarmPermissionDialog: State<Boolean> = _showExactAlarmPermissionDialog

    private val _isEditMode = MutableStateFlow(false)
    val isEditMode: StateFlow<Boolean> = _isEditMode

    private val noteIdArg: Int = savedStateHandle["noteId"] ?: -1
    private var noteId = -1

    private val _uiState = mutableStateOf(AddNoteUiState())
    val uiState: AddNoteUiState get() = _uiState.value

    val checklistItems = mutableStateListOf<ChecklistItem>()

    fun onTitleChanged(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle)
    }

    fun onContentChanged(newContent: String) {
        _uiState.value = _uiState.value.copy(content = newContent)
    }

    fun onColorSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedColorIndex = index)
    }

    fun addChecklistItem(text: String) {
        checklistItems.add(0, ChecklistItem(text))
        sortChecklist()
    }

    fun removeChecklistItem(index: Int) {
        if (index in checklistItems.indices) checklistItems.removeAt(index)
    }

    fun toggleChecklistItem(index: Int) {
        val item = checklistItems[index]
        checklistItems[index] = item.copy(isChecked = !item.isChecked)
        sortChecklist()
    }

    fun setNoteType(type: NoteType) {
        _uiState.value = _uiState.value.copy(noteType = type)
    }

    fun updateChecklistItemText(index: Int, newText: String) {
        if (index in checklistItems.indices) {
            checklistItems[index] = checklistItems[index].copy(text = newText)
        }
    }

    private fun sortChecklist() {
        val sorted = checklistItems.sortedWith(
            compareBy { it.isChecked }
        )
        checklistItems.clear()
        checklistItems.addAll(sorted)
    }

    fun showReminderSheet(show: Boolean) {
        _uiState.value = _uiState.value.copy(isReminderSheetVisible = show)
    }

    fun setReminderTime(timestamp: Long?) {
        _uiState.value = _uiState.value.copy(reminderTime = timestamp)
    }

    fun tryScheduleReminder(note: Note) {
        if (!canScheduleExactAlarms(context)) {
            _showExactAlarmPermissionDialog.value = true
            return
        }

        try {
            reminderScheduler.schedule(note)
        } catch (e: SecurityException) {
            _showExactAlarmPermissionDialog.value = true
        }
    }

    fun onExactAlarmPermissionDialogDismissed() {
        _showExactAlarmPermissionDialog.value = false
    }

    fun onOpenExactAlarmSettings() {
        openExactAlarmPermissionSettings(context)
    }

    fun loadNoteIfAvailable() {
        if (noteIdArg != -1 && noteId != noteIdArg) {
            _isEditMode.value = true
            noteId = noteIdArg
            viewModelScope.launch {
                val note = noteUseCases.getNoteById(noteIdArg).firstOrNull()
                note?.let {
                    val currentTime = System.currentTimeMillis()
                    val isReminderExpired = it.reminderTime != null && it.reminderTime < currentTime
                    val updatedReminderTime = if (isReminderExpired) null else it.reminderTime

                    _uiState.value = _uiState.value.copy(
                        title = it.title,
                        content = it.content,
                        noteType = it.noteType,
                        reminderTime = updatedReminderTime,
                        selectedColorIndex = noteColors.indexOfFirst { color ->
                            color.toArgb() == Color(it.colorHex.toColorInt()).toArgb()
                        }.takeIf { i -> i != -1 } ?: 0
                    )

                    checklistItems.clear()
                    checklistItems.addAll(it.checklistItems)
                    sortChecklist()

                    if (isReminderExpired) {
                        noteUseCases.addNote(it.copy(reminderTime = null))
                    }
                }
            }
        }
    }

    fun saveNote() {
        val current = _uiState.value

        val isTextNoteEmpty = current.noteType == NoteType.TEXT && current.content.isBlank()
        val isChecklistNoteEmpty = current.noteType == NoteType.CHECKLIST && checklistItems.isEmpty()

        if (current.title.isBlank() && isTextNoteEmpty || isChecklistNoteEmpty) {
            return
        }

        val isEditing = noteId != -1

        viewModelScope.launch {
            val existingNote = if (isEditing) {
                noteUseCases.getNoteById(noteId).firstOrNull()
            } else null

            val newReminderTime = current.reminderTime
            val existingReminderTime = existingNote?.reminderTime

            if (existingReminderTime != null && newReminderTime == null) {
                reminderScheduler.cancel(existingNote.id)
            } else if (existingReminderTime != null && existingReminderTime != newReminderTime) {
                reminderScheduler.cancel(existingNote.id)
            }

            val note = Note(
                id = if (isEditing) noteId else 0,
                title = current.title,
                content = current.content,
                noteType = current.noteType,
                checklistItems = checklistItems.toList(),
                timestamp = System.currentTimeMillis(),
                colorHex = String.format(
                    "#%06X",
                    0xFFFFFF and noteColors[current.selectedColorIndex].toArgb()
                ),
                isPinned = existingNote?.isPinned ?: false,
                isArchived = existingNote?.isArchived ?: false,
                reminderTime = newReminderTime

            )

            noteUseCases.addNote(note)
            if (newReminderTime != null) {
                tryScheduleReminder(note)
            }
            _uiState.value = current.copy(saveSuccess = true)


        }
    }
}