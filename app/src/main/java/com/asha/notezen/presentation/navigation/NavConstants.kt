package com.asha.notezen.presentation.navigation

object NavConstants {
    const val ARG_NOTE_ID = "noteId"
    const val ADD_NOTE_ROUTE = "add_note"
    const val NOTE_LIST_ROUTE = "note_list"
    const val ARCHIVED_LIST_ROUTE = "archived_notes"
    const val ADD_NOTE_ROUTE_WITH_ARG = "$ADD_NOTE_ROUTE?$ARG_NOTE_ID={$ARG_NOTE_ID}"
}