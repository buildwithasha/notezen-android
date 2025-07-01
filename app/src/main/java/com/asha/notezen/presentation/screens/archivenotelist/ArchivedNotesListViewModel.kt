package com.asha.notezen.presentation.screens.archivenotelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchivedNotesListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _recentlyDeletedNote = MutableStateFlow<Note?>(null)
    val recentlyDeletedNote: StateFlow<Note?> = _recentlyDeletedNote

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    val archivedNotes = noteUseCases.getArchivedNotes()

    val filteredNotes: StateFlow<List<Note>> = combine(
        archivedNotes,
        searchQuery
    ) { notes, query ->
        if (query.isBlank()) notes
        else notes.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.content.contains(query, ignoreCase = true)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
            _recentlyDeletedNote.value = note
        }
    }

    fun clearRecentlyDeletedNote() {
        _recentlyDeletedNote.value = null
    }

    fun restoreDeletedNote() {
        viewModelScope.launch {
            recentlyDeletedNote.value?.let {
                noteUseCases.addNote(it)
            }
            _recentlyDeletedNote.value = null
        }
    }

    fun toggleArchive(note: Note) {
        viewModelScope.launch {
            val updated = note.copy(isArchived = !note.isArchived)
            noteUseCases.addNote(updated)
        }
    }
}
