package uz.alimov.notesapp.presentation.screen.category

import uz.alimov.notesapp.domain.model.CategoryData

data class CategoryUiState(
    val categoryList: List<CategoryData> = emptyList(),
    val addEditSheetMode: AddEditSheetMode = AddEditSheetMode.ADD,
    var addingEditingCategory: CategoryData = CategoryData(
        id = 0,
        name = ""
    ),
    val showBottomSheet: Boolean = false,
    val bottomSheetType: CategoryBottomSheetType? = null,
    val selectedCategoryId: Long? = null
) {
    val isListEmpty: Boolean get() = categoryList.size < 2
}