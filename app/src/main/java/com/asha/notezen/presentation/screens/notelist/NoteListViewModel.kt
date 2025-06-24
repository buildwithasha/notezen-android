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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteListUiState())
    val uiState: StateFlow<NoteListUiState> = _uiState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery


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
            noteUseCases.deleteNote(note)
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
        applyFiltersAndSort()
    }

    private fun applyFiltersAndSort() {
            viewModelScope.launch {
                noteUseCases.getNotes().collect { notes ->
                    val sorted = noteUseCases.filterAndSortNotes(
                        notes = notes,
                        query = _uiState.value.searchQuery,
                        sortType = _uiState.value.sortType,
                        sortOrder = _uiState.value.sortOrder
                    )
                    _uiState.update { it.copy(notes = sorted) }
                }
            }
        }
    }