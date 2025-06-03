package com.asha.notezen.presentation.screens.addnote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.asha.notezen.domain.model.Note
import com.asha.notezen.domain.usecase.NoteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class AddNoteViewModel @Inject constructor(
    private val noteUseCases: NoteUseCases
) : ViewModel() {

    var title by mutableStateOf("")
        private set

    var content by mutableStateOf("")
        private set

    var saveSuccess by mutableStateOf(false)
        private set

    fun onTitleChange(newTitle: String) {
        title = newTitle
    }

    fun onContentChanged(newContent: String) {
        content = newContent
    }

    var selectedColor by mutableStateOf(Color.White.toArgb()) // default
        private set

    fun onColorSelected(color: Int) {
        selectedColor = color
    }
    fun saveNote() {
        if (title.isBlank() && content.isBlank()) return
        viewModelScope.launch {
            noteUseCases.addNote(
                Note(
                    title = title,
                    content = content,
                    timestamp = System.currentTimeMillis(),
                    colorHex = String.format("#%06X", 0xFFFFFF and selectedColor)
                )
            )
            saveSuccess = true
        }
    }
}