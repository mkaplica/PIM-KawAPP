package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.database.model.BrewMethodEntity

/** Access to the grinders table. */
@Dao
interface BrewMethodDao {
    /** All brewMethods in the order they were added. Emits again whenever the table changes. */
    @Query("SELECT * FROM brew_methods ORDER BY createdAt ASC")
    fun getBrewMethods(): Flow<List<BrewMethodEntity>>

    /** Inserts [brewMethod], replacing one with the same id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrewMethod(brewMethod: BrewMethodEntity)

    /** Deletes the brewMethos with [id]. Does nothing if it does not exist. */
    @Query("DELETE FROM brew_methods WHERE id = :id")
    suspend fun deleteBrewMethod(id: String)
}
