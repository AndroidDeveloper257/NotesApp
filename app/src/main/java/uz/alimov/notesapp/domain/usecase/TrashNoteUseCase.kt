package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.repository.NotesRepository

class TrashNoteUseCase(
    private val repository: NotesRepository
) {
    suspend fun invoke(id: Long) {
        repository.trash(id)
    }
}