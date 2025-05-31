package uz.alimov.notesapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.domain.repository.CategoryRepository

class GetCategoryUseCase(
    private val repository: CategoryRepository
) {
    fun invoke(id: Long): Flow<Resource<CategoryData, String>> {
        return repository.get(id)
    }
}