package uz.alimov.notesapp.data.module

import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import uz.alimov.notesapp.presentation.screen.category.CategoryViewModel
import uz.alimov.notesapp.presentation.screen.home.HomeViewModel
import uz.alimov.notesapp.presentation.screen.note.NoteViewModel

val viewModelModule = module {
    viewModel {
        HomeViewModel(
            getAllNotesUseCase = get(),
            getNotesByCategoryUseCase = get(),
            getCategoriesUseCase = get(),
            deleteNoteUseCase = get()
        )
    }
    viewModel {
        NoteViewModel(
            addNoteUseCase = get(),
            getNoteUseCase = get(),
            getCategoriesUseCase = get(),
            updateNoteUseCase = get(),
            deleteNoteUseCase = get(),
            getDefaultCategoryUseCase = get()
        )
    }
    viewModel {
        CategoryViewModel(
            getCategoriesUseCase = get(),
            addCategoryUseCase = get(),
            updateCategoryUseCase = get(),
            deleteCategoryUseCase = get()
        )
    }
}