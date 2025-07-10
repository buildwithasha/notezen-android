package com.asha.notezen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.asha.notezen.data.local.converters.ChecklistConverter
import com.asha.notezen.data.local.dao.NoteDao
import com.asha.notezen.data.local.entity.NoteEntity

@Database(entities = [NoteEntity::class], version = 4, exportSchema = true)
@TypeConverters(ChecklistConverter::class)

abstract class NoteDataBase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    }