package com.asha.notezen.data.mapper

import com.asha.notezen.data.local.entity.NoteEntity
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.model.NoteType

fun NoteEntity.toDomain(): Note = Note(id, title, content, NoteType.valueOf(noteType), checklistItems,timestamp, colorHex, isPinned, isArchived, reminderTime)
fun Note.toEntity(): NoteEntity = NoteEntity(id, title, content, noteType.name, checklistItems, timestamp, colorHex, isPinned, isArchived, reminderTime)