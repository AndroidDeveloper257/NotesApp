package uz.alimov.notesapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "ALTER TABLE category_table ADD COLUMN is_default INTEGER NOT NULL DEFAULT 0"
        )
    }
}