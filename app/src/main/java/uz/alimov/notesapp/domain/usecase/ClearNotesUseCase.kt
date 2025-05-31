package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.repository.NotesRepository

class ClearNotesUseCase(
    private val repository: NotesRepository
) {
    suspend fun invoke() {
        repository.clear()
    }
}