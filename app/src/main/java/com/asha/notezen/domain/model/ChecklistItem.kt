package com.asha.notezen.domain.model

data class ChecklistItem(
    val text: String,
    val isChecked: Boolean = false
)