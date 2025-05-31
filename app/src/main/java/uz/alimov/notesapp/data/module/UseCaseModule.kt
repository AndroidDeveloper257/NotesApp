package uz.alimov.notesapp.data.module

import org.koin.dsl.module
import uz.alimov.notesapp.domain.usecase.AddCategoryUseCase
import uz.alimov.notesapp.domain.usecase.AddNoteUseCase
import uz.alimov.notesapp.domain.usecase.ChangeNoteCategoryUseCase
import uz.alimov.notesapp.domain.usecase.ClearCategoriesUseCase
import uz.alimov.notesapp.domain.usecase.ClearNotesUseCase
import uz.alimov.notesapp.domain.usecase.DeleteCategoryUseCase
import uz.alimov.notesapp.domain.usecase.DeleteNoteUseCase
import uz.alimov.notesapp.domain.usecase.GetAllNotesUseCase
import uz.alimov.notesapp.domain.usecase.GetAllTrashedNotesUseCase
import uz.alimov.notesapp.domain.usecase.GetCategoriesUseCase
import uz.alimov.notesapp.domain.usecase.GetCategoryUseCase
import uz.alimov.notesapp.domain.usecase.GetDefaultCategoryUseCase
import uz.alimov.notesapp.domain.usecase.GetNoteUseCase
import uz.alimov.notesapp.domain.usecase.GetNotesByCategoryUseCase
import uz.alimov.notesapp.domain.usecase.TrashNoteUseCase
import uz.alimov.notesapp.domain.usecase.UpdateCategoryUseCase
import uz.alimov.notesapp.domain.usecase.UpdateNoteUseCase

val useCaseModule = module {
    single {
        AddCategoryUseCase(get())
    }
    single {
        AddNoteUseCase(get())
    }
    single {
        ChangeNoteCategoryUseCase(get())
    }
    single {
        ClearCategoriesUseCase(get())
    }
    single {
        ClearNotesUseCase(get())
    }
    single {
        DeleteCategoryUseCase(
            categoryRepository = get(),
            notesRepository = get()
        )
    }
    single {
        DeleteNoteUseCase(get())
    }
    single {
        GetAllNotesUseCase(get())
    }
    single {
        GetAllTrashedNotesUseCase(get())
    }
    single {
        GetCategoriesUseCase(get())
    }
    single {
        GetCategoryUseCase(get())
    }
    single {
        GetNotesByCategoryUseCase(get())
    }
    single {
        GetNoteUseCase(get())
    }
    single {
        TrashNoteUseCase(get())
    }
    single {
        UpdateCategoryUseCase(get())
    }
    single {
        UpdateNoteUseCase(get())
    }
    single {
        GetDefaultCategoryUseCase(get())
    }
}