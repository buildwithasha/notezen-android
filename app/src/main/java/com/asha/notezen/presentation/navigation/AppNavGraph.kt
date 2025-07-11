package com.asha.notezen.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.asha.notezen.presentation.screens.addnote.AddNoteScreen
import com.asha.notezen.presentation.screens.addnote.AddNoteViewModel
import com.asha.notezen.presentation.screens.archivenotelist.composables.ArchivedNotesScreen
import com.asha.notezen.presentation.screens.notelist.NoteListScreen


@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    )
    {
        composable(Screen.NoteList.route) {
            NoteListScreen(navController = navController)
        }

        composable(
            route = NavConstants.ADD_NOTE_ROUTE_WITH_ARG,
            arguments = listOf(
                navArgument(NavConstants.ARG_NOTE_ID) {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val viewModel: AddNoteViewModel = hiltViewModel(backStackEntry)
            AddNoteScreen(navController = navController, viewModel = viewModel)
        }

        composable(Screen.ArchivedNotes.route) {
            ArchivedNotesScreen(navController = navController)
        }
    }
}
