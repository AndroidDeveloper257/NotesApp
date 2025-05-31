package uz.alimov.notesapp.data.module

import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import uz.alimov.notesapp.data.local.MIGRATION_1_2
import uz.alimov.notesapp.data.local.NotesDatabase
import uz.alimov.notesapp.data.local.entity.CategoryEntity

val databaseModule = module {
    single {
        Room.databaseBuilder(
            context = androidContext(),
            klass = NotesDatabase::class.java,
            name = "notes_database"
        )
            .addMigrations(MIGRATION_1_2)
            .addCallback(
                object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            val categoryDao = get<NotesDatabase>().categoryDao()
                            val count = categoryDao.getCount()
                            if (count == 0) {
                                categoryDao.add(
                                    CategoryEntity(
                                        name = "Uncategorized",
                                        isDefault = true
                                    )
                                )
                            }
                        }
                    }
                }
            )
            .build()
    }
    single {
        get<NotesDatabase>().notesDao()
    }
    single {
        get<NotesDatabase>().categoryDao()
    }
}