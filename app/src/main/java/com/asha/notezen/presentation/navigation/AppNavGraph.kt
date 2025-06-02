package com.asha.notezen.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.asha.notezen.presentation.screens.addnote.AddNoteScreen
import com.asha.notezen.presentation.screens.notelist.NoteListScreen

sealed class Screen(val route: String) {
    object noteList : Screen("note_list")
    object AddNote : Screen("add_note")
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.noteList.route
    ) {
        composable(Screen.noteList.route) {
            NoteListScreen(navController = navController)
        }
        composable(Screen.AddNote.route) {
            AddNoteScreen(navController = navController)
        }
    }
}
