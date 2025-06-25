package com.asha.notezen.presentation.navigation

sealed class Screen(val route: String) {

    object NoteList : Screen(NavConstants.NOTE_LIST_ROUTE)

    object AddNote : Screen(NavConstants.ADD_NOTE_ROUTE) {
        fun passNoteId(noteId: Int): String {
            return "${NavConstants.ADD_NOTE_ROUTE}?${NavConstants.ARG_NOTE_ID}=$noteId"
        }
    }

    object ArchivedNotes : Screen(NavConstants.ARCHIVED_LIST_ROUTE)
}
