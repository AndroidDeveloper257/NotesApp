package uz.alimov.notesapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.NoteData

interface NotesRepository {

    suspend fun add(note: NoteData): Long

    fun get(id: Long): Flow<Resource<NoteData, String>>

    suspend fun update(note: NoteData)

    suspend fun delete(id: Long)

    suspend fun trash(id: Long)

    suspend fun changeCategory(
        categoryId: Long,
        noteId: Long
    )

    fun getAll(): Flow<Resource<List<NoteData>, String>>

    fun getAllTrashed(): Flow<Resource<List<NoteData>, String>>

    fun getByCategory(categoryId: Long): Flow<Resource<List<NoteData>, String>>

    suspend fun moveNotesToDefault(deletingCategoryId: Long): Int

    suspend fun clear()

}