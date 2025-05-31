package uz.alimov.notesapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uz.alimov.notesapp.data.local.dao.NotesDao
import uz.alimov.notesapp.data.mapper.toNoteData
import uz.alimov.notesapp.data.mapper.toNotesEntity
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.NoteData
import uz.alimov.notesapp.domain.repository.NotesRepository

class NotesRepositoryImpl(
    private val notesDao: NotesDao
) : NotesRepository {
    override suspend fun add(note: NoteData): Long {
        return if (note.title.trim().isNotEmpty() || note.body.trim().isNotEmpty()) {
            notesDao.add(note.toNotesEntity())
        } else 0L
    }

    override fun get(id: Long): Flow<Resource<NoteData, String>> {
        val noteFlow = notesDao.get(id)
        return flow {
            noteFlow
                .catch {
                    emit(Resource.Error(it.message.toString()))
                }
                .collect {
                    emit(Resource.Success(it.toNoteData()))
                }
        }
    }

    override suspend fun update(note: NoteData) {
        if (note.title.trim().isNotEmpty() || note.body.trim().isNotEmpty()) {
            notesDao.update(note.toNotesEntity())
        }
    }

    override suspend fun delete(id: Long) {
        notesDao.delete(id)
    }

    override suspend fun trash(id: Long) {
        notesDao.trash(id)
    }

    override suspend fun changeCategory(categoryId: Long, noteId: Long) {
        notesDao.changeCategory(categoryId, noteId)
    }

    override fun getAll(): Flow<Resource<List<NoteData>, String>> {
        val allNotesFlow = notesDao.getAll()
        return flow {
            allNotesFlow
                .catch {
                    emit(Resource.Error(it.message.toString()))
                }
                .collect { noteEntities ->
                    emit(
                        Resource.Success(noteEntities.map {
                            it.toNoteData()
                        })
                    )
                }
        }
    }

    override fun getAllTrashed(): Flow<Resource<List<NoteData>, String>> {
        val trashedNotesFlow = notesDao.getAllTrashed()
        return flow {
            trashedNotesFlow
                .catch {
                    emit(Resource.Error(it.message.toString()))
                }
                .collect { trashedNoteEntities ->
                    emit(
                        Resource.Success(
                            trashedNoteEntities.map {
                                it.toNoteData()
                            }
                        )
                    )
                }
        }
    }

    override fun getByCategory(categoryId: Long): Flow<Resource<List<NoteData>, String>> {
        val notesByCategory = notesDao.getByCategory(categoryId)
        return flow {
            notesByCategory
                .catch {
                    emit(Resource.Error(it.message.toString()))
                }
                .collect { noteEntitiesByCategory ->
                    emit(
                        Resource.Success(
                            noteEntitiesByCategory.map {
                                it.toNoteData()
                            }
                        )
                    )
                }
        }
    }

    override suspend fun moveNotesToDefault(deletingCategoryId: Long): Int {
        return notesDao.moveNotesToDefault(deletingCategoryId)
    }

    override suspend fun clear() {
        notesDao.clear()
    }
}