package com.asha.notezen.presentation.screens.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.usecase.NoteUseCases
import com.asha.notezen.domain.util.SortOrder
import com.asha.notezen.domain.util.SortType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState: StateFlow<NoteListUiState> = _uiState


    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            noteUseCases.getNotes().collect { notes ->
                _uiState.update {
                    val sorted = sortList(
                        notes = notes,
                        type = it.sortType,
                        order = it.sortOrder
                    )
                    it.copy(notes = sorted)
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }

    fun toggleSortSection() {
        _uiState.update { it.copy(isSortSectionVisible = !it.isSortSectionVisible) }
    }

    fun updateSort(type: SortType? = null, order: SortOrder? = null) {
        _uiState.update {
            val newSortType = type ?: it.sortType
            val newSortOrder = order ?: it.sortOrder
            val sortedNotes = sortList(it.notes, newSortType, newSortOrder)
            it.copy(
                sortType = newSortType,
                sortOrder = newSortOrder,
                notes = sortedNotes
            )
        }
    }

    fun hideSortSection() {
        _uiState.update { it.copy(isSortSectionVisible = false) }
    }

    private fun sortList(
        notes: List<Note>,
        type: SortType,
        order: SortOrder
    ): List<Note> {
        val sorted = when (type) {
            SortType.TITLE -> notes.sortedBy { it.title.lowercase() }
            SortType.DATE -> notes.sortedBy { it.timestamp }
            SortType.COLOR -> notes.sortedBy { it.colorHex }
        }
        return if (order == SortOrder.DESCENDING) sorted.reversed() else sorted
    }
}