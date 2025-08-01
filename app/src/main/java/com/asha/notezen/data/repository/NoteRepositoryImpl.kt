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

    override fun getNotes(): Flow<List<Note>> =
        dao.getNotesOrderedWithPinned().map { it.map { entity -> entity.toDomain() } }


    override suspend fun insertNote(note: Note): Long {
        return dao.insertNote(note.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.deleteNote(note.toEntity())
    }

    override fun getNoteById(id: Int): Flow<Note?> {
        return dao.getNoteById(id).map {
            it?.toDomain()
        }
    }

    override fun getNotesOrderedWithPinned(): Flow<List<Note>> = dao.getNotesOrderedWithPinned().map { list ->
        list.map {
            it.toDomain()
        }

    }

    override fun getArchivedNotes(): Flow<List<Note>> =
        dao.getArchivedNotes().map { list -> list.map { it.toDomain() } }

}
