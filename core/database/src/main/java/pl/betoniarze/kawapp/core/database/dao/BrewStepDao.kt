package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.betoniarze.kawapp.core.database.model.BrewStepEntity

@Dao
interface BrewStepDao {
    @Query("SELECT * FROM brew_steps")
    suspend fun getAllBrewSteps(): List<BrewStepEntity>

    @Query("SELECT * FROM brew_steps WHERE id = :id")
    suspend fun getBrewStepById(id: String): BrewStepEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrewStep(feedPost: BrewStepEntity)

    @Delete
    suspend fun deleteBrewStep(id: String)
}