package uz.alimov.notesapp.domain.usecase

import uz.alimov.notesapp.domain.model.NoteData
import uz.alimov.notesapp.domain.repository.NotesRepository

class AddNoteUseCase(
    private val repository: NotesRepository
) {
    suspend fun invoke(note: NoteData): Long {
        return repository.add(note)
    }
}