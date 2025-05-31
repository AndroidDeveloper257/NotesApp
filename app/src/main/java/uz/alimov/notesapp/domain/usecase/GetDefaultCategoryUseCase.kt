package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.repository.CategoryRepository

class GetDefaultCategoryUseCase(
    private val categoryRepository: CategoryRepository
) {
    fun invoke() = categoryRepository.getDefault()
}