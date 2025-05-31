package uz.alimov.notesapp.presentation.screen.category

sealed class CategoryEffect {
    data class NavigateBack(val id: Long?): CategoryEffect()
    data class ShowError(val message: String): CategoryEffect()
}