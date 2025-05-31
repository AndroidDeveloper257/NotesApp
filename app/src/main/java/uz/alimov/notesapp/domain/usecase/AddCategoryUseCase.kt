package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.domain.repository.CategoryRepository

class AddCategoryUseCase(
    private val repository: CategoryRepository
) {
    suspend fun invoke(category: CategoryData) {
        repository.add(category)
    }
}