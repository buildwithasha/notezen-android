package com.asha.notezen.presentation.screens.notelist

import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.util.SortOrder
import com.asha.notezen.domain.util.SortType

data class NoteListUiState(
    val notes: List<Note> = emptyList(),
    val sortType: SortType = SortType.DATE,
    val sortOrder: SortOrder = SortOrder.DESCENDING,
    val isSortSectionVisible: Boolean = false,
    val searchQuery: String = ""
)