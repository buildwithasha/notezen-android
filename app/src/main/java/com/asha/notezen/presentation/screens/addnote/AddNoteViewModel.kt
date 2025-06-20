package com.asha.notezen.presentation.screens.addnote

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.toColorInt
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.usecase.NoteUseCases
import com.asha.notezen.presentation.ui.theme.noteColors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteIdArg: Int = savedStateHandle["noteId"] ?: -1
    private var noteId = -1

    private val _uiState = mutableStateOf(AddNoteUiState())
    val uiState: AddNoteUiState get() = _uiState.value

    fun onTitleChange(newTitle: String) {
        _uiState.value = _uiState.value.copy(title = newTitle)
    }

    fun onContentChanged(newContent: String) {
        _uiState.value = _uiState.value.copy(content = newContent)
    }

    fun onColorSelected(index: Int) {
        _uiState.value = _uiState.value.copy(selectedColorIndex = index)
    }

    fun loadNoteIfAvailable() {
        if (noteIdArg != -1 && noteId != noteIdArg) {
            noteId = noteIdArg
            viewModelScope.launch {
                val note = noteUseCases.getNoteById(noteIdArg).firstOrNull()
                note?.let {
                    _uiState.value = _uiState.value.copy(
                        title = it.title,
                        content = it.content,
                        selectedColorIndex = noteColors.indexOfFirst { color ->
                            color.toArgb() == Color(it.colorHex.toColorInt()).toArgb()
                        }.takeIf { i -> i != -1 } ?: 0
                    )
                }
            }
        }
    }

    fun saveNote() {
        val current = _uiState.value
        if (current.title.isBlank() && current.content.isBlank()) return

        val isEditing = noteId != -1
        viewModelScope.launch {
            val note = Note(
                id = if (isEditing) noteId else 0,
                title = current.title,
                content = current.content,
                timestamp = System.currentTimeMillis(),
                colorHex = String.format("#%06X", 0xFFFFFF and noteColors[current.selectedColorIndex].toArgb())
            )
            noteUseCases.addNote(note)
            _uiState.value = current.copy(saveSuccess = true)
        }
    }
}