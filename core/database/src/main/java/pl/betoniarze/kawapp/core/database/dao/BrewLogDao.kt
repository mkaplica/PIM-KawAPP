package pl.betoniarze.kawapp.core.database.dao

import pl.betoniarze.kawapp.core.database.model.BrewLogEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface BrewLogDao {
    @Query("SELECT * FROM brew_logs ORDER BY date DESC, time DESC")
    fun getAllBrewLogs(): Flow<List<BrewLogEntity>>

    @Query("SELECT * FROM brew_logs WHERE id = :id ORDER BY date DESC, time DESC")
    fun getBrewLogById(id: String): Flow<BrewLogEntity?>

    @Query("SELECT DISTINCT date FROM brew_logs")
    fun getDatesWithBrews(): Flow<List<LocalDate>>

    @Query("SELECT * FROM brew_logs WHERE date = :date ORDER BY date DESC, time DESC")
    fun getBrewLogsForDate(date: LocalDate): Flow<List<BrewLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrewLog(brewLog: BrewLogEntity)

    @Query("DELETE FROM brew_logs WHERE id = :id")
    suspend fun deleteBrewLog(id: String)
}