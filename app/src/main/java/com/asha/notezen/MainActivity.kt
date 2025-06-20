package com.asha.notezen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.asha.notezen.presentation.navigation.AppNavGraph
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
                AppNavGraph(navController = navController)
            }
        }
    }
}