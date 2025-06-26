package com.asha.notezen.presentation.screens.notelist.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Search notes...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = {
                    onValueChange("")
                    coroutineScope.launch {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                }) {
                    Icon(Icons.Default.Close, contentDescription = "Clear Search")
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .focusRequester(focusRequester),
        singleLine = true
    )
}
