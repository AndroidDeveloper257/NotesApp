package uz.alimov.notesapp.presentation.screen.category

import uz.alimov.notesapp.domain.model.CategoryData

sealed class CategoryIntent {
    object LoadCategories : CategoryIntent()
    object NavigateUpClicked : CategoryIntent()
    object AddCategoryClicked : CategoryIntent()
    object SaveCategoryConfirmed : CategoryIntent()
    object SaveCategoryCanceled : CategoryIntent()
    data class CategoryClicked(val id: Long) : CategoryIntent()
    data class CategoryLongClicked(val data: CategoryData) : CategoryIntent()
    data class EditCategoryClicked(val data: CategoryData) : CategoryIntent()
    data class DeleteCategoryClicked(val id: Long) : CategoryIntent()
    data class DeleteCategoryConfirmed(val id: Long) : CategoryIntent()
    object DeleteCategoryCanceled : CategoryIntent()
    data class CategoryNameChanged(val name: String) : CategoryIntent()
    object OptionsDismissed : CategoryIntent()
    data class SelectCategory(val id: Long) : CategoryIntent()
}