package com.asha.notezen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.asha.notezen.presentation.screens.addnote.AddNoteScreen
import com.asha.notezen.presentation.screens.notelist.NoteListScreen
import com.asha.notezen.presentation.ui.theme.NoteZenTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NoteZenTheme {
                val navController = rememberNavController()
                NoteAppNavHost(navController)
            }
        }
    }

    @Composable
    fun NoteAppNavHost(navController: NavHostController) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

            NavHost(
                navController = navController, startDestination = "note_list",
                modifier = Modifier.padding(innerPadding)
            ) {

                composable("note_list") {
                    NoteListScreen(navController)
                }

                composable("add_note") {
                    AddNoteScreen(navController)
                }
            }

        }

    }

}