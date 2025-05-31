package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.repository.NotesRepository

class ChangeNoteCategoryUseCase(
    private val repository: NotesRepository
) {
    suspend fun invoke(
        categoryId: Long,
        noteId: Long
    ) {
        repository.changeCategory(categoryId, noteId)
    }
}