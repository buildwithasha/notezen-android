package com.asha.notezen.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.asha.notezen.presentation.screens.addnote.AddNoteScreen
import com.asha.notezen.presentation.screens.notelist.NoteListScreen


@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.NoteList.route
    ) {
        // Note List Screen
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
        ) {
            AddNoteScreen(navController = navController)
        }
    }
}
