package uz.alimov.notesapp.presentation.screen.note

sealed class NoteEffect {
    object NavigateBack: NoteEffect()
    data class ShowError(val message: String): NoteEffect()
}