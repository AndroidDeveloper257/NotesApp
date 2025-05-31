package uz.alimov.notesapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.NoteData
import uz.alimov.notesapp.domain.repository.NotesRepository

class GetNoteUseCase(
    private val repository: NotesRepository
) {
    fun invoke(id: Long): Flow<Resource<NoteData, String>> {
        return repository.get(id)
    }
}