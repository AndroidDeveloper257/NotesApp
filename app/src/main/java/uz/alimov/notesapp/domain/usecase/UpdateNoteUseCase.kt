package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.model.NoteData
import uz.alimov.notesapp.domain.repository.NotesRepository

class UpdateNoteUseCase(
    private val repository: NotesRepository
) {
    suspend fun invoke(note: NoteData) {
        repository.update(note)
    }
}