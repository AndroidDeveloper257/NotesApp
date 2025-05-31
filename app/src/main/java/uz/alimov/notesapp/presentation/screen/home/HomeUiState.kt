package uz.alimov.notesapp.presentation.screen.home

import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.domain.model.NoteData

data class HomeUiState(
    val noteList: List<NoteData> = emptyList(),
    val categoryList: List<CategoryData> = emptyList(),
    val selectedCategoryId: Long = 0,
    val showOptionsSheet: Boolean = false,
    val showDeleteSheet: Boolean = false,
    val selectedNote: NoteData? = null
) {
    val isNoteListEmpty: Boolean get() = noteList.isEmpty()
    val isCategoryListEmpty: Boolean get() = categoryList.size <= 1
}