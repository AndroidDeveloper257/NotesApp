package uz.alimov.notesapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "notes_table",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["category_id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class NotesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    val id: Long = 0,
    @ColumnInfo(name = "note_title")
    val title: String,
    @ColumnInfo(name = "note_body")
    val body: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "character_count")
    val charCount: Long = 0,
    @ColumnInfo(name = "line_count")
    val lineCount: Long = 0,
    @ColumnInfo(name = "word_count")
    val wordCount: Long = 0,
    @ColumnInfo(name = "is_pinned")
    val isPinned: Boolean = false,
    @ColumnInfo(name = "is_trashed")
    val isTrashed: Boolean = false,
    @ColumnInfo(name = "category_id")
    val categoryId: Long = 0
)