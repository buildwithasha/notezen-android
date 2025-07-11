package com.asha.notezen.presentation.screens.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.usecase.NoteUseCases
import com.asha.notezen.domain.util.SortOrder
import com.asha.notezen.domain.util.SortType
import com.asha.notezen.reminder.ReminderScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    private val reminderScheduler: ReminderScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState: StateFlow<NoteListUiState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _recentlyDeletedNote = MutableStateFlow<Note?>(null)
    val recentlyDeletedNote: StateFlow<Note?> = _recentlyDeletedNote


    init {
        observeNotes()
    }

    private fun observeNotes() {
        viewModelScope.launch {
            combine(
                noteUseCases.getNotes(),
                searchQuery,
                _uiState.map { it.sortType },
                _uiState.map { it.sortOrder }
            ) { notes, query, sortType, sortOrder ->
                noteUseCases.filterAndSortNotes(
                    notes = notes,
                    query = query,
                    sortType = sortType,
                    sortOrder = sortOrder
                )
            }.collect { result ->
                _uiState.update { it.copy(notes = result) }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {

            if (note.reminderTime != null) {
                reminderScheduler.cancel(note.id)
            }
            noteUseCases.deleteNote(note)
            _recentlyDeletedNote.value = note
        }
    }

    fun clearRecentlyDeletedNote() {
        _recentlyDeletedNote.value = null
    }

    fun restoreDeletedNote() {
        viewModelScope.launch {
            recentlyDeletedNote.value?.let { note ->
                noteUseCases.addNote(note)

                if (note.reminderTime != null && note.reminderTime > System.currentTimeMillis()) {
                    reminderScheduler.schedule(note)
                }
            }
            _recentlyDeletedNote.value = null
        }
    }


    fun toggleSortSection() {
        _uiState.update { it.copy(isSortSectionVisible = !it.isSortSectionVisible) }
    }

    fun hideSortSection() {
        _uiState.update { it.copy(isSortSectionVisible = false) }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun updateSort(type: SortType? = null, order: SortOrder? = null) {
        _uiState.update {
            it.copy(
                sortType = type ?: it.sortType,
                sortOrder = order ?: it.sortOrder
            )
        }

    }

    fun togglePin(note: Note) {
        viewModelScope.launch {
            val updatedNote = note.copy(isPinned = !note.isPinned)
            noteUseCases.addNote(updatedNote)
        }
    }

    fun archiveNote(note: Note) {
        viewModelScope.launch {
            val archivedNote = note.copy(isArchived = true)
            noteUseCases.addNote(archivedNote)
        }
    }
}