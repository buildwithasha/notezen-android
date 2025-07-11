package com.asha.notezen.presentation.screens.addnote

import com.asha.notezen.domain.model.NoteType

data class AddNoteUiState(
    val title: String = "",
    val content: String = "",
    val selectedColorIndex: Int = 0,
    val noteType: NoteType = NoteType.TEXT,
    val saveSuccess: Boolean = false,
    val reminderTime: Long? = null,
    val isReminderSheetVisible: Boolean = false
)