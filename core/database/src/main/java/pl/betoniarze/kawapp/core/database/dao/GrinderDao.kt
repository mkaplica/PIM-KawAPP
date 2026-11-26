package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.database.model.GrinderEntity

/** Access to the grinders table. */
@Dao
interface GrinderDao {
    /** All grinders in the order they were added. Emits again whenever the table changes. */
    @Query("SELECT * FROM grinders ORDER BY createdAt ASC")
    fun getGrinders(): Flow<List<GrinderEntity>>

    /** Inserts [grinder], replacing one with the same id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrinder(grinder: GrinderEntity)

    /** Deletes the grinder with [id]. Does nothing if it does not exist. */
    @Query("DELETE FROM grinders WHERE id = :id")
    suspend fun deleteGrinder(id: String)
}
