package uz.alimov.notesapp.presentation.screen.home

import uz.alimov.notesapp.domain.model.NoteData

sealed class HomeIntent {
    object LoadNotes : HomeIntent()
    data class NoteClicked(val note: NoteData) : HomeIntent()
    object AddNoteClicked : HomeIntent()
    object CategoryActionClicked : HomeIntent()
    object SettingsActionClicked : HomeIntent()
    object LoadCategories : HomeIntent()
    data class CategoryClicked(val id: Long) : HomeIntent()
    data class NoteLongClicked(val note: NoteData) : HomeIntent()
    object OptionsDismissed : HomeIntent()
    object EditClicked : HomeIntent()
    object DeleteClicked : HomeIntent()
    object DeleteCanceled : HomeIntent()
    object DeleteConfirmed : HomeIntent()
}