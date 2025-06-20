package com.asha.notezen.presentation.screens.notelist

import com.asha.notezen.domain.model.Note

data class NoteListUiState(
    val notes: List<Note> = emptyList()
)