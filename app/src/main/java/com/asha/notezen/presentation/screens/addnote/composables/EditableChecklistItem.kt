package com.asha.notezen.presentation.screens.addnote.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.asha.notezen.domain.model.ChecklistItem

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EditableChecklistItem(
    item: ChecklistItem,
    index: Int,
    onToggle: (Int) -> Unit,
    onRemove: (Int) -> Unit,
    onTextChange: (Int, String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    var isEditing by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf(item.text) }


    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(IntrinsicSize.Min)
    ) {
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = { onToggle(index) }
        )

        val commonModifier = Modifier
            .weight(1f)
            .padding(end = 8.dp)

        if (isEditing) {
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                    keyboardController?.show()
                }

                BasicTextField(
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                    },
                    singleLine = true,
                    modifier = commonModifier
                        .focusRequester(focusRequester),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onTextChange(index, textFieldValue)
                            isEditing = false
                        }
                    ),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            innerTextField()
                        }
                    }
                )
        } else {
            Text(
                text = item.text,
                modifier = commonModifier.clickable {
                    isEditing = true
                    textFieldValue = item.text
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        IconButton(onClick = { onRemove(index) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete item")
        }
    }
}