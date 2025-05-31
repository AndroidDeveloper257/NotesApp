package uz.alimov.notesapp.presentation.screen.category

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
import uz.alimov.notesapp.domain.model.CategoryData
import uz.alimov.notesapp.domain.usecase.AddCategoryUseCase
import uz.alimov.notesapp.domain.usecase.DeleteCategoryUseCase
import uz.alimov.notesapp.domain.usecase.GetCategoriesUseCase
import uz.alimov.notesapp.domain.usecase.UpdateCategoryUseCase

class CategoryViewModel(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState get() = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CategoryEffect>()
    val effect get() = _effect.asSharedFlow()

    init {
        onIntent(CategoryIntent.LoadCategories)
    }

    fun onIntent(intent: CategoryIntent) {
        when (intent) {
            CategoryIntent.AddCategoryClicked -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = true,
                        bottomSheetType = CategoryBottomSheetType.ADD_EDIT,
                        addEditSheetMode = AddEditSheetMode.ADD,
                        addingEditingCategory = CategoryData(
                            id = 0,
                            name = ""
                        )
                    )
                }
            }

            is CategoryIntent.CategoryClicked -> {
                sendEffect(CategoryEffect.NavigateBack(intent.id))
            }

            is CategoryIntent.CategoryLongClicked -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = true,
                        bottomSheetType = CategoryBottomSheetType.DELETE_EDIT_OPTIONS,
                        addEditSheetMode = AddEditSheetMode.EDIT,
                        addingEditingCategory = intent.data,
                        selectedCategoryId = intent.data.id
                    )
                }
            }

            CategoryIntent.DeleteCategoryCanceled -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = true,
                        bottomSheetType = CategoryBottomSheetType.DELETE_EDIT_OPTIONS
                    )
                }
            }

            is CategoryIntent.DeleteCategoryClicked -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = true,
                        bottomSheetType = CategoryBottomSheetType.DELETE
                    )
                }
            }

            is CategoryIntent.DeleteCategoryConfirmed -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = false,
                        bottomSheetType = null,
                        selectedCategoryId = null
                    )
                }
                deleteCategory(intent.id)
            }

            is CategoryIntent.EditCategoryClicked -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = true,
                        bottomSheetType = CategoryBottomSheetType.ADD_EDIT,
                        addEditSheetMode = AddEditSheetMode.EDIT,
                        addingEditingCategory = intent.data
                    )
                }
            }

            CategoryIntent.LoadCategories -> {
                loadCategories()
            }

            CategoryIntent.NavigateUpClicked -> {
                sendEffect(CategoryEffect.NavigateBack(null))
            }

            CategoryIntent.SaveCategoryCanceled -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = false,
                        addingEditingCategory = CategoryData(
                            id = 0,
                            name = ""
                        ),
                        bottomSheetType = null,
                        selectedCategoryId = null
                    )
                }
            }

            CategoryIntent.SaveCategoryConfirmed -> {
                saveCategory()
                _uiState.update {
                    it.copy(
                        showBottomSheet = false,
                        addingEditingCategory = CategoryData(
                            id = 0,
                            name = ""
                        ),
                        bottomSheetType = null,
                        selectedCategoryId = null
                    )
                }
            }

            is CategoryIntent.CategoryNameChanged -> {
                _uiState.update { categoryUiState ->
                    categoryUiState.copy(
                        addingEditingCategory = categoryUiState.addingEditingCategory.copy(
                            name = intent.name
                        )
                    )
                }
            }

            CategoryIntent.OptionsDismissed -> {
                _uiState.update {
                    it.copy(
                        showBottomSheet = false,
                        bottomSheetType = null,
                        selectedCategoryId = null
                    )
                }
            }

            is CategoryIntent.SelectCategory -> {
                _uiState.update {
                    it.copy(
                        selectedCategoryId = intent.id
                    )
                }
            }
        }
    }

    private fun deleteCategory(categoryId: Long) {
        viewModelScope.launch {
            deleteCategoryUseCase.invoke(categoryId)
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            getCategoriesUseCase
                .invoke()
                .catch {
                    _uiState.update {
                        it.copy(
                            categoryList = emptyList()
                        )
                    }
                    sendEffect(CategoryEffect.ShowError(it.message ?: "Failed to load categories"))
                }
                .collect { resource ->
                    when (resource) {
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    categoryList = emptyList()
                                )
                            }
                            sendEffect(CategoryEffect.ShowError(resource.rawResponse))
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

    private fun saveCategory() {
        viewModelScope.launch {
            val stateValue = _uiState.value
            when (stateValue.addEditSheetMode) {
                AddEditSheetMode.ADD -> {
                    addCategoryUseCase.invoke(
                        category = CategoryData(
                            name = stateValue.addingEditingCategory.name
                        )
                    )
                }

                AddEditSheetMode.EDIT -> {
                    updateCategoryUseCase.invoke(
                        category = CategoryData(
                            id = stateValue.addingEditingCategory.id,
                            name = stateValue.addingEditingCategory.name
                        )
                    )
                }
            }
        }
    }

    private fun sendEffect(effect: CategoryEffect) {
        viewModelScope.launch {
            _effect.emit(effect)
        }
    }

}