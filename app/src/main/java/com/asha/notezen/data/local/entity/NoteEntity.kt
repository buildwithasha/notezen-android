package com.asha.notezen.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.asha.notezen.domain.model.ChecklistItem
import com.asha.notezen.domain.model.NoteType

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val noteType: String = NoteType.TEXT.name,
    val checklistItems: List<ChecklistItem> = emptyList(),
    val timestamp: Long,
    val colorHex: String,
    val isPinned: Boolean = false,
    val isArchived: Boolean = false,
    val reminderTime: Long? = null

)