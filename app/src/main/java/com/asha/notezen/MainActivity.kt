package com.asha.notezen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.asha.notezen.presentation.navigation.AppNavGraph
import com.asha.notezen.presentation.navigation.Screen
import com.asha.notezen.presentation.ui.theme.NoteZenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val noteIdFromIntent = intent?.getIntExtra("noteId", -1)

        setContent {
            NoteZenTheme {
                val navController = rememberNavController()

                AppNavGraph(
                    navController = navController,
                    startDestination = Screen.NoteList.route
                )
                LaunchedEffect(noteIdFromIntent) {
                    if (noteIdFromIntent != null && noteIdFromIntent != -1) {
                        navController.navigate(Screen.AddNote.passNoteId(noteIdFromIntent))
                    }
                }
            }
        }
    }
}