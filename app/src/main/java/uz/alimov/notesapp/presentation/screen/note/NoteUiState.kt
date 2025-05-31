package uz.alimov.notesapp.presentation.screen.note

import uz.alimov.notesapp.domain.model.CategoryData

data class NoteUiState(
    val noteId: Long = 0,
    val title: String = "",
    val body: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val actionsExpanded: Boolean = false,
    val showCategorySheet: Boolean = false,
    val selectedCategoryId: Long? = null,
    val categoryList: List<CategoryData> = emptyList(),
    val inputsFocused: Boolean = false
)