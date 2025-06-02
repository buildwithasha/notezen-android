package com.asha.notezen.data.mapper

import com.asha.notezen.data.local.entity.NoteEntity
import com.asha.notezen.domain.model.Note

fun NoteEntity.toDomain(): Note = Note(id, title, content, timestamp, colorHex)
fun Note.toEntity(): NoteEntity = NoteEntity(id, title, content, timestamp, colorHex)