package com.asha.notezen.presentation.screens.archivenotelist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArchivedNotesListViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    val archivedNotes: StateFlow<List<Note>> =
        noteUseCases.getArchivedNotes()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            noteUseCases.deleteNote(note)
        }
    }

    fun toggleArchive(note: Note) {
        viewModelScope.launch {
            val updated = note.copy(isArchived = !note.isArchived)
            noteUseCases.addNote(updated)
        }
    }
}
