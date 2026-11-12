package pl.betoniarze.kawapp.core.database.dao

import pl.betoniarze.kawapp.core.database.model.BrewLogEntity

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface BrewLogDao {
    @Query("SELECT * FROM brew_logs")
    suspend fun getAllBrewLogs(): List<BrewLogEntity>

    @Query("SELECT * FROM brew_logs WHERE id = :id")
    suspend fun getBrewLogById(id: String): BrewLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrewLog(brewLog: BrewLogEntity)

    @Delete
    suspend fun deleteBrewLog(id: String)
}