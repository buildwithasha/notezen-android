package com.asha.notezen.di

import android.app.Application
import androidx.room.Room
import com.asha.notezen.data.local.NoteDataBase
import com.asha.notezen.data.local.dao.NoteDao
import com.asha.notezen.data.repository.NoteRepositoryImpl
import com.asha.notezen.domain.repository.NoteRepository
import com.asha.notezen.domain.usecase.AddNoteUseCase
import com.asha.notezen.domain.usecase.DeleteNoteUseCase
import com.asha.notezen.domain.usecase.FilterAndSortNotesUseCase
import com.asha.notezen.domain.usecase.GetNoteByIdUseCase
import com.asha.notezen.domain.usecase.GetNotesUseCase
import com.asha.notezen.domain.usecase.NoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)

object AppModule {

    @Provides
    @Singleton
    fun provideDataBase(app: Application): NoteDataBase =
        Room.databaseBuilder(app, NoteDataBase::class.java, "note_db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideNoteDao(db: NoteDataBase): NoteDao = db.noteDao()

    @Provides
    @Singleton
    fun provideRepository(dao: NoteDao): NoteRepository = NoteRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideUseCases(repository: NoteRepository): NoteUseCases = NoteUseCases(
        getNotes = GetNotesUseCase(repository),
        addNote = AddNoteUseCase(repository),
        deleteNote = DeleteNoteUseCase(repository),
        getNoteById = GetNoteByIdUseCase(repository),
        filterAndSortNotes = FilterAndSortNotesUseCase()
    )

}