package com.asha.notezen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.asha.notezen.data.local.dao.NoteDao
import com.asha.notezen.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao
}