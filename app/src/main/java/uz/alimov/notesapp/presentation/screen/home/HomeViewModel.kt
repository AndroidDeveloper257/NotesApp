package uz.alimov.notesapp.presentation.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.usecase.DeleteNoteUseCase
import uz.alimov.notesapp.domain.usecase.GetAllNotesUseCase
import uz.alimov.notesapp.domain.usecase.GetCategoriesUseCase
import uz.alimov.notesapp.domain.usecase.GetNotesByCategoryUseCase
import uz.alimov.notesapp.presentation.screen.home.HomeEffect.NavigateToCategory
import uz.alimov.notesapp.presentation.screen.home.HomeEffect.NavigateToNote
import uz.alimov.notesapp.presentation.screen.home.HomeEffect.NavigateToSettings
import uz.alimov.notesapp.presentation.screen.home.HomeEffect.ShowError

class HomeViewModel(
    private val getAllNotesUseCase: GetAllNotesUseCase,
    private val getNotesByCategoryUseCase: GetNotesByCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<HomeEffect>()
    val effect get() = _effect.asSharedFlow()

    init {
        onIntent(HomeIntent.LoadNotes)
        onIntent(HomeIntent.LoadCategories)
    }

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.AddNoteClicked -> {
                sendEffect(NavigateToNote(null))
            }

            HomeIntent.LoadNotes -> {
                loadNotes()
            }

            is HomeIntent.NoteClicked -> {
                sendEffect(NavigateToNote(intent.note))
            }

            HomeIntent.CategoryActionClicked -> {
                sendEffect(NavigateToCategory)
            }

            HomeIntent.SettingsActionClicked -> {
                sendEffect(NavigateToSettings)
            }

            is HomeIntent.CategoryClicked -> {
                _uiState.update {
                    it.copy(
                        selectedCategoryId = intent.id
                    )
                }
                filterNotesByCategory(intent.id)
            }

            HomeIntent.LoadCategories -> {
                loadCategories()
            }

            HomeIntent.DeleteCanceled -> {
                _uiState.update {
                    it.copy(
                        showDeleteSheet = false,
                        selectedNote = null
                    )
                }
            }

            HomeIntent.DeleteClicked -> {
                _uiState.update {
                    it.copy(
                        showDeleteSheet = true,
                        showOptionsSheet = false
                    )
                }
            }

            HomeIntent.DeleteConfirmed -> {
                deleteNote(_uiState.value.selectedNote?.id ?: 0)
                _uiState.update {
                    it.copy(
                        showDeleteSheet = false,
                        selectedNote = null
                    )
                }
            }

            HomeIntent.EditClicked -> {
                _uiState.update {
                    it.copy(
                        showOptionsSheet = false
                    )
                }
                sendEffect(NavigateToNote(_uiState.value.selectedNote))
            }

            is HomeIntent.NoteLongClicked -> {
                _uiState.update {
                    it.copy(
                        selectedNote = intent.note,
                        showOptionsSheet = true
                    )
                }
            }

            HomeIntent.OptionsDismissed -> {
                _uiState.update {
                    it.copy(
                        selectedNote = null,
                        showOptionsSheet = false
                    )
                }
            }
        }
    }

    private fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            deleteNoteUseCase.invoke(noteId)
        }
    }

    private fun loadNotes() {
        viewModelScope.launch {
            getAllNotesUseCase.invoke()
                .catch {
                    _uiState.update {
                        it.copy(
                            noteList = emptyList()
                        )
                    }
                    sendEffect(ShowError(it.message ?: "Failed to load notes"))
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    noteList = emptyList()
                                )
                            }
                            sendEffect(ShowError(resource.rawResponse))
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    noteList = resource.data
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun filterNotesByCategory(categoryId: Long) {
        if (categoryId == 0L) {
            loadNotes()
            return
        }
        viewModelScope.launch {
            getNotesByCategoryUseCase.invoke(categoryId)
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    noteList = emptyList()
                                )
                            }
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    noteList = resource.data.filter { note ->
                                        note.categoryId == categoryId
                                    }
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase.invoke()
                .catch {
                    _uiState.update {
                        it.copy(
                            categoryList = emptyList()
                        )
                    }
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    categoryList = emptyList()
                                )
                            }
                        }

                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    categoryList = resource.data,
                                    selectedCategoryId = 0
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun sendEffect(effect: HomeEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

}