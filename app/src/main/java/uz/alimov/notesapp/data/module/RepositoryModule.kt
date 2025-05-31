package uz.alimov.notesapp.data.module

import org.koin.dsl.module
import uz.alimov.notesapp.data.repository.CategoryRepositoryImpl
import uz.alimov.notesapp.data.repository.NotesRepositoryImpl
import uz.alimov.notesapp.domain.repository.CategoryRepository
import uz.alimov.notesapp.domain.repository.NotesRepository

val repositoryModule = module {
    single<NotesRepository> {
        NotesRepositoryImpl(get())
    }
    single<CategoryRepository> {
        CategoryRepositoryImpl(get())
    }
}