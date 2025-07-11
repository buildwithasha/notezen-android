package com.asha.notezen.domain.model

data class Note(
    val id: Int = 0,
    val title: String,
    val content: String,
    val noteType: NoteType = NoteType.TEXT,
    val checklistItems: List<ChecklistItem> = emptyList(),
    val timestamp: Long,
    val colorHex: String,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val reminderTime: Long? = null
)
