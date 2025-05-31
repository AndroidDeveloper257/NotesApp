package uz.alimov.notesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import uz.alimov.notesapp.data.local.entity.NotesEntity

@Dao
interface NotesDao {

    @Insert(
        entity = NotesEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun add(note: NotesEntity): Long

    @Query("SELECT * FROM notes_table WHERE note_id = :id")
    fun get(id: Long): Flow<NotesEntity>

    @Update
    suspend fun update(note: NotesEntity)

    @Query("DELETE FROM notes_table WHERE note_id = :id")
    suspend fun delete(id: Long)

    @Query("UPDATE notes_table SET is_trashed = 1 WHERE note_id = :id")
    suspend fun trash(id: Long)

    @Query("UPDATE notes_table SET category_id = :categoryId WHERE note_id = :noteId")
    suspend fun changeCategory(
        categoryId: Long,
        noteId: Long
    )

    @Query("SELECT * FROM notes_table WHERE is_trashed = 0")
    fun getAll(): Flow<List<NotesEntity>>

    @Query("SELECT * FROM notes_table WHERE is_trashed = 1")
    fun getAllTrashed(): Flow<List<NotesEntity>>

    @Query("SELECT * FROM notes_table WHERE category_id = :categoryId")
    fun getByCategory(categoryId: Long): Flow<List<NotesEntity>>

    @Query(
        "UPDATE notes_table SET category_id = (SELECT category_id from category_table WHERE is_default = 1 LIMIT 1) WHERE category_id = :deletingCategoryId"
    )
    suspend fun moveNotesToDefault(deletingCategoryId: Long): Int

    @Query("DELETE FROM notes_table")
    suspend fun clear()

}