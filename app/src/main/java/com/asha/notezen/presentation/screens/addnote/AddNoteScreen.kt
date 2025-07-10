package com.asha.notezen.presentation.screens.addnote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.domain.model.NoteType
import com.asha.notezen.presentation.screens.addnote.composables.ChecklistAddField
import com.asha.notezen.presentation.screens.addnote.composables.ColorPickerRow
import com.asha.notezen.presentation.screens.addnote.composables.EditableChecklistItem
import com.asha.notezen.presentation.screens.addnote.composables.NoteTypeToggle
import com.asha.notezen.presentation.ui.theme.noteColors

@Composable
fun AddNoteScreen(
    navController: NavController,
    viewModel: AddNoteViewModel = hiltViewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(Unit) {
        viewModel.loadNoteIfAvailable()
    }

    if (state.saveSuccess) {
        LaunchedEffect(Unit) {
            navController.popBackStack()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = noteColors[state.selectedColorIndex],
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.saveNote() },
                containerColor = Color.White,
                contentColor = Color.Black,
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(6.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = "Save Note")
            }
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(noteColors[state.selectedColorIndex])
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 16.dp)
        ) {
            ColorPickerRow(
                noteColors = noteColors,
                selectedIndex = state.selectedColorIndex,
                onColorSelected = viewModel::onColorSelected
            )

            Spacer(modifier = Modifier.height(12.dp))

            NoteTypeToggle(
                selectedType = state.noteType,
                onTypeSelected = viewModel::setNoteType
            )

            Spacer(modifier = Modifier.height(12.dp))

            TextField(
                value = state.title,
                onValueChange = viewModel::onTitleChanged,
                placeholder = {
                    Text("Title", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                when (state.noteType) {
                    NoteType.TEXT -> {
                        item {
                            TextField(
                                value = state.content,
                                onValueChange = viewModel::onContentChanged,
                                placeholder = { Text("Note content...", fontSize = 16.sp) },
                                textStyle = TextStyle(fontSize = 16.sp),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    disabledContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledIndicatorColor = Color.Transparent
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    NoteType.CHECKLIST -> {
                        itemsIndexed(viewModel.checklistItems) { index, item ->
                            EditableChecklistItem(
                                item = item,
                                index = index,
                                onToggle = viewModel::toggleChecklistItem,
                                onRemove = viewModel::removeChecklistItem,
                                onTextChange = viewModel::updateChecklistItemText
                            )
                        }

                        item {
                            ChecklistAddField(
                                onAdd = viewModel::addChecklistItem
                            )
                        }
                    }
                }
            }
        }
    }
}
