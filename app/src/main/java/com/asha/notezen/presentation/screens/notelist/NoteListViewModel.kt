package com.asha.notezen.presentation.screens.notelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.usecase.NoteUseCases
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
                _uiState.update { it.copy(notes = notes) }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }
}
