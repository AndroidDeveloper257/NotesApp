package uz.alimov.notesapp.presentation.screen.home

import uz.alimov.notesapp.domain.model.NoteData

sealed class HomeEffect {
    data class NavigateToNote(val note: NoteData? = null): HomeEffect()
    data class ShowError(val message: String): HomeEffect()
    object NavigateToCategory: HomeEffect()
    object NavigateToSettings: HomeEffect()
}