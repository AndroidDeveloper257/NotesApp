package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.repository.CategoryRepository

class ClearCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke() {
        repository.clear()
    }
}