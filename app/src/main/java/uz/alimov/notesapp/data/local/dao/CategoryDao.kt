package uz.alimov.notesapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import uz.alimov.notesapp.data.local.entity.CategoryEntity

@Dao
interface CategoryDao {

    @Insert(
        entity = CategoryEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun add(category: CategoryEntity)

    @Query("SELECT * FROM category_table WHERE category_id = :id")
    fun get(id: Long): Flow<CategoryEntity>

    @Update
    suspend fun update(category: CategoryEntity)

    @Query("DELETE FROM category_table WHERE category_id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM category_table")
    fun getAll(): Flow<List<CategoryEntity>>

    @Query("DELETE FROM category_table")
    suspend fun clear()

    @Query("SELECT COUNT(*) FROM category_table")
    suspend fun getCount(): Int

    @Query("SELECT * FROM category_table WHERE is_default = 1")
    fun getDefault(): Flow<CategoryEntity>
}