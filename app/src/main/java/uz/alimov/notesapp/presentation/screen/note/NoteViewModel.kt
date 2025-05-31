package uz.alimov.notesapp.presentation.screen.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uz.alimov.notesapp.domain.Resource
import uz.alimov.notesapp.domain.model.NoteData
import uz.alimov.notesapp.domain.usecase.AddNoteUseCase
import uz.alimov.notesapp.domain.usecase.DeleteNoteUseCase
import uz.alimov.notesapp.domain.usecase.GetCategoriesUseCase
import uz.alimov.notesapp.domain.usecase.GetDefaultCategoryUseCase
import uz.alimov.notesapp.domain.usecase.GetNoteUseCase
import uz.alimov.notesapp.domain.usecase.UpdateNoteUseCase

class NoteViewModel(
    private val addNoteUseCase: AddNoteUseCase,
    private val getNoteUseCase: GetNoteUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val getDefaultCategoryUseCase: GetDefaultCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<NoteEffect>()
    val effect get() = _effect.asSharedFlow()

    init {
        onIntent(NoteIntent.LoadCategories)
    }

    fun onIntent(intent: NoteIntent) {
        when (intent) {
            is NoteIntent.BodyChanged -> {
                _uiState.update { it.copy(body = intent.body) }
                saveNote()
            }

            is NoteIntent.LoadNote -> {
                loadNote(intent.noteId)
            }

            is NoteIntent.TitleChanged -> {
                _uiState.update { it.copy(title = intent.title) }
                saveNote()
            }

            NoteIntent.ActionsClicked -> {
                _uiState.update {
                    it.copy(
                        actionsExpanded = true
                    )
                }
            }

            NoteIntent.CategoryClicked -> {
                _uiState.update {
                    it.copy(
                        actionsExpanded = false,
                        showCategorySheet = true
                    )
                }
            }

            is NoteIntent.CategorySelected -> {
                Log.d("TAG", "onIntent: NoteIntent.CategorySelected ${intent.categoryId}")
                if (_uiState.value.selectedCategoryId != intent.categoryId) {
                    _uiState.update {
                        it.copy(
                            selectedCategoryId = intent.categoryId,
                            showCategorySheet = false
                        )
                    }
                    saveNote()
                }
            }

            NoteIntent.DeleteClicked -> {
                _uiState.update {
                    it.copy(
                        actionsExpanded = false
                    )
                }
                deleteNote()
                sendEffect(NoteEffect.NavigateBack)
            }

            NoteIntent.NavigateBackClicked -> {
                sendEffect(NoteEffect.NavigateBack)
            }

            NoteIntent.ActionsDismissed -> {
                _uiState.update {
                    it.copy(
                        actionsExpanded = false
                    )
                }
            }

            NoteIntent.CategorySheetDismissed -> {
                _uiState.update {
                    it.copy(
                        showCategorySheet = false
                    )
                }
            }

            is NoteIntent.LoadCategories -> {
                loadCategories()
            }

            is NoteIntent.InputFocusChanged -> {
                _uiState.update {
                    it.copy(
                        inputsFocused = intent.isFocused
                    )
                }
            }

            NoteIntent.DoneClicked -> {
                _uiState.update {
                    it.copy(
                        inputsFocused = false
                    )
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase.invoke()
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
                                    categoryList = resource.data
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun deleteNote() {
        viewModelScope.launch {
            deleteNoteUseCase.invoke(_uiState.value.noteId)
        }
    }

    private fun loadNote(noteId: Long?) {
        if (noteId == null) {
            getDefaultCategory()
            return
        }
        viewModelScope.launch {
            getNoteUseCase.invoke(noteId)
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .catch { throwable ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = throwable.message) }
                    _effect.emit(NoteEffect.ShowError(throwable.message ?: "Failed to load note"))
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = resource.rawResponse
                                )
                            }
                            _effect.emit(NoteEffect.ShowError(resource.rawResponse))
                        }

                        is Resource.Success -> {
                            val data = resource.data
                            _uiState.update {
                                it.copy(
                                    noteId = data.id ?: 0,
                                    title = data.title,
                                    body = data.body,
                                    isLoading = false,
                                    selectedCategoryId = data.categoryId
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun getDefaultCategory() {
        viewModelScope.launch {
            getDefaultCategoryUseCase.invoke()
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
                                    selectedCategoryId = resource.data.id,
                                    noteId = 0,
                                    title = "",
                                    body = "",
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            try {
                if (_uiState.value.noteId == 0L) {
                    val addedNotId = addNoteUseCase.invoke(
                        note = NoteData(
                            id = _uiState.value.noteId,
                            title = _uiState.value.title,
                            body = _uiState.value.body,
                            categoryId = _uiState.value.selectedCategoryId ?: 0
                        )
                    )
                    _uiState.update {
                        it.copy(
                            noteId = addedNotId
                        )
                    }
                } else {
                    updateNoteUseCase.invoke(
                        note = NoteData(
                            id = _uiState.value.noteId,
                            title = _uiState.value.title,
                            body = _uiState.value.body,
                            categoryId = _uiState.value.selectedCategoryId ?: 0
                        )
                    )
                }
            } catch (_: Exception) {
                _effect.emit(NoteEffect.ShowError("Failed to save note"))
            }
        }
    }

    private fun sendEffect(effect: NoteEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

}