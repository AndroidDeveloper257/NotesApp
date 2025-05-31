package uz.alimov.notesapp.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.NoteData
import uz.alimov.notesapp.domain.repository.NotesRepository

class GetNotesByCategoryUseCase(
    private val repository: NotesRepository
) {
    fun invoke(categoryId: Long) : Flow<Resource<List<NoteData>, String>> {
        return repository.getByCategory(categoryId)
    }
}