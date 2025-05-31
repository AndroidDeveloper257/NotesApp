package uz.alimov.notesapp.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.CategoryData

interface CategoryRepository {

    suspend fun add(category: CategoryData)

    fun get(id: Long): Flow<Resource<CategoryData, String>>

    suspend fun update(category: CategoryData)

    suspend fun delete(id: Long)

    fun getAll(): Flow<Resource<List<CategoryData>, String>>

    suspend fun clear()

    fun getDefault(): Flow<Resource<CategoryData, String>>

}