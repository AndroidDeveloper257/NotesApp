package uz.alimov.notesapp.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import uz.alimov.notesapp.data.local.dao.CategoryDao
import uz.alimov.notesapp.data.mapper.toCategoryData
import uz.alimov.notesapp.data.mapper.toCategoryEntity
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.domain.repository.CategoryRepository

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override suspend fun add(category: CategoryData) {
        if (category.name.trim().isNotEmpty()) {
            categoryDao.add(
                category.copy(
                    name = category.name.trim()
                ).toCategoryEntity()
            )
        }
    }

    override fun get(id: Long): Flow<Resource<CategoryData, String>> {
        val categoryFlow = categoryDao.get(id)
        return flow {
            categoryFlow
                .catch {
                    emit(Resource.Error(it.message.toString()))
                }
                .collect {
                    emit(Resource.Success(it.toCategoryData()))
                }
        }
    }

    override suspend fun update(category: CategoryData) {
        categoryDao.update(category.toCategoryEntity())
    }

    override suspend fun delete(id: Long) {
        categoryDao.delete(id)
    }

    override fun getAll(): Flow<Resource<List<CategoryData>, String>> {
        val categoriesEntityFlow = categoryDao.getAll()
        return flow {
            categoriesEntityFlow
                .catch {
                    emit(Resource.Error(it.message.toString()))
                }
                .collect { categoryEntities ->
                    emit(
                        Resource.Success(
                            categoryEntities.map {
                                it.toCategoryData()
                            }
                        )
                    )
                }
        }
    }

    override suspend fun clear() {
        categoryDao.clear()
    }

    override fun getDefault(): Flow<Resource<CategoryData, String>> {
        val categoryFlow = categoryDao.getDefault()
        return flow {
            categoryFlow
                .catch {
                    emit(Resource.Error(it.message.toString()))
                }
                .collect {
                    emit(Resource.Success(it.toCategoryData()))
                }
        }
    }
}