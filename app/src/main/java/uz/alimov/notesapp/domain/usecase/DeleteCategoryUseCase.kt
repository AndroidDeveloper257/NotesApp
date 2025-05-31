package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.repository.CategoryRepository
import uz.alimov.notesapp.domain.repository.NotesRepository

class DeleteCategoryUseCase(
    private val categoryRepository: CategoryRepository,
    private val notesRepository: NotesRepository
) {
    suspend fun invoke(id: Long) {
        if (notesRepository.moveNotesToDefault(id) >= 0) {
            categoryRepository.delete(id)
        }
    }
}