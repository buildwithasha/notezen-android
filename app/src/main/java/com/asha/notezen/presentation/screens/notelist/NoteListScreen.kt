package com.asha.notezen.presentation.screens.notelist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColorInt
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.asha.notezen.domain.util.SortOrder
import com.asha.notezen.domain.util.SortType
import com.asha.notezen.presentation.navigation.Screen
import com.asha.notezen.presentation.screens.composables.SortRadioButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteListScreen(
    navController: NavController,
    viewModel: NoteListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()
    val previousSize = remember { mutableIntStateOf(0) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.notes.size) {
        viewModel.hideSortSection()
        if (uiState.notes.isNotEmpty() && uiState.notes.size > previousSize.intValue) {
            listState.animateScrollToItem(0)
        }
        previousSize.intValue = uiState.notes.size
    }

    val showScrollToTop by remember {
        derivedStateOf { listState.firstVisibleItemIndex > 3 }
    }

    Scaffold(
        floatingActionButton = {
            Column(
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp),
                horizontalAlignment = Alignment.End
            ) {
                if (showScrollToTop) {
                    FloatingActionButton(
                        onClick = {
                            scope.launch {
                                listState.animateScrollToItem(0)
                            }
                        },
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Scroll to Top")
                    }
                }

                FloatingActionButton(
                    onClick = { navController.navigate(Screen.AddNote.route) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Note")
                }
            }
        }
    )
    { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your Notes",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold
                    )
                )

                IconButton(onClick = {
                    viewModel.toggleSortSection()
                }) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort Notes",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            AnimatedVisibility(visible = uiState.isSortSectionVisible) {

                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                ) {

                    // First row: SortType
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        SortRadioButton(
                            label = "Title",
                            selected = uiState.sortType == SortType.TITLE
                        ) { viewModel.updateSort(type = SortType.TITLE) }

                        SortRadioButton(
                            label = "Date",
                            selected = uiState.sortType == SortType.DATE
                        ) { viewModel.updateSort(type = SortType.DATE) }

                        SortRadioButton(
                            label = "Color",
                            selected = uiState.sortType == SortType.COLOR
                        ) { viewModel.updateSort(type = SortType.COLOR) }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Second row: SortOrder
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        SortRadioButton(
                            label = "Ascending",
                            selected = uiState.sortOrder == SortOrder.ASCENDING
                        ) { viewModel.updateSort(order = SortOrder.ASCENDING) }

                        SortRadioButton(
                            label = "Descending",
                            selected = uiState.sortOrder == SortOrder.DESCENDING
                        ) { viewModel.updateSort(order = SortOrder.DESCENDING) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                items(uiState.notes) { note ->
                    val backgroundColor = Color(note.colorHex.toColorInt())
                    val textColor =
                        if (backgroundColor.luminance() < 0.5f) Color.White else Color.Black

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable(
                                onClick = {
                                    navController.navigate(Screen.AddNote.passNoteId(note.id))
                                }
                            ),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = backgroundColor)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = note.title,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )

                            if (note.content.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = note.content,
                                    fontSize = 14.sp,
                                    color = textColor.copy(alpha = 0.95f),
                                    maxLines = 6
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = formatDate(note.timestamp),
                                    fontSize = 12.sp,
                                    color = textColor.copy(alpha = 0.7f),
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Note",
                                    tint = textColor.copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable {
                                            viewModel.deleteNote(note)
                                        }
                                )
                            }

                        }
                    }
                }
            }
        }
    }
}
