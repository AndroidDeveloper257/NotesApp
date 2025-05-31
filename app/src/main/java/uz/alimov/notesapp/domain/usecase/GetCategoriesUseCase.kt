package uz.alimov.notesapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.domain.repository.CategoryRepository

class GetCategoriesUseCase(
    private val repository: CategoryRepository
) {
    fun invoke(): Flow<Resource<List<CategoryData>, String>> {
        return repository.getAll()
    }
}