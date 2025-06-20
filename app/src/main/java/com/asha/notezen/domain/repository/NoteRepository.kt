package com.asha.notezen.domain.repository

import com.asha.notezen.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun getNotes(): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    fun getNoteById(id: Int): Flow<Note?>
}