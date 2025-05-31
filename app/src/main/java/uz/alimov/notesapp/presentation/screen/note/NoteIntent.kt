package uz.alimov.notesapp.presentation.screen.note

sealed class NoteIntent {
    object NavigateBackClicked: NoteIntent()
    data class TitleChanged(val title: String): NoteIntent()
    data class BodyChanged(val body: String): NoteIntent()
    data class LoadNote(val noteId: Long?): NoteIntent()
    object LoadCategories: NoteIntent()
    object ActionsClicked: NoteIntent()
    object ActionsDismissed: NoteIntent()
    object DeleteClicked: NoteIntent()
    object CategoryClicked: NoteIntent()
    data class CategorySelected(val categoryId: Long): NoteIntent()
    object CategorySheetDismissed: NoteIntent()
    data class InputFocusChanged(val isFocused: Boolean): NoteIntent()
    object DoneClicked: NoteIntent()
}