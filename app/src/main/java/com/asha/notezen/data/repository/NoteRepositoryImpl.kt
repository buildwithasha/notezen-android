package com.asha.notezen.data.repository

import com.asha.notezen.data.local.dao.NoteDao
import com.asha.notezen.data.mapper.toDomain
import com.asha.notezen.data.mapper.toEntity
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override fun getNotes(): Flow<List<Note>> = dao.getAllNotes().map { list ->
        list.map {
            it.toDomain()
        }
    }

    override suspend fun insertNote(note: Note) {
        dao.insertNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }

    override fun getNoteById(id: Int): Flow<Note?> {
        return dao.getNoteById(id).map {
            it?.toDomain()
        }
    }
}
