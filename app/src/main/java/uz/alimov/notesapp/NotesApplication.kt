package uz.alimov.notesapp

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import uz.alimov.notesapp.data.module.databaseModule
import uz.alimov.notesapp.data.module.repositoryModule
import uz.alimov.notesapp.data.module.useCaseModule
import uz.alimov.notesapp.data.module.viewModelModule

class NotesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@NotesApplication)
            modules(
                databaseModule,
                repositoryModule,
                useCaseModule,
                viewModelModule
            )
        }
    }
}