package uz.alimov.notesapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.alimov.notesapp.data.local.dao.CategoryDao
import uz.alimov.notesapp.data.local.dao.NotesDao
import uz.alimov.notesapp.data.local.entity.CategoryEntity
import uz.alimov.notesapp.data.local.entity.NotesEntity

@Database(
    entities = [
        NotesEntity::class,
        CategoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NotesDatabase() : RoomDatabase() {

    abstract fun notesDao(): NotesDao
    abstract fun categoryDao(): CategoryDao

}