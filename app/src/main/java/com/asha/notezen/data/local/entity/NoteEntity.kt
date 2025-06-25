package com.asha.notezen.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long,
    val colorHex: String,

    @ColumnInfo(defaultValue = "0")
    val isPinned: Boolean = false
)