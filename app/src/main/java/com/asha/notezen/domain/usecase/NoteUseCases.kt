package com.asha.notezen.domain.usecase

import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow

data class NoteUseCases(
    val getNotes: GetNotesUseCase,
    val addNote: AddNoteUseCase,
    val deleteNote: DeleteNoteUseCase,
    val getNoteById: GetNoteByIdUseCase
)

class GetNotesUseCase(private val repository: NoteRepository) {
    operator fun invoke(): Flow<List<Note>> = repository.getNotes()
}

class AddNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.insertNote(note)
}

class DeleteNoteUseCase(private val repository: NoteRepository) {
    suspend operator fun invoke(note: Note) = repository.deleteNote(note)
}

class GetNoteByIdUseCase(private val repository: NoteRepository) {
    operator fun invoke(id: Int): Flow<Note?> = repository.getNoteById(id)
}

