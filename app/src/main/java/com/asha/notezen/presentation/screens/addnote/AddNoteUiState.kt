package com.asha.notezen.presentation.screens.addnote

data class AddNoteUiState(
    val title: String = "",
    val content: String = "",
    val selectedColorIndex: Int = 0,
    val saveSuccess: Boolean = false
)