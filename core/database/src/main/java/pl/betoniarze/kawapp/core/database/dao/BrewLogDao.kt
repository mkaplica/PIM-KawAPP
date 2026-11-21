package pl.betoniarze.kawapp.core.database.dao

import pl.betoniarze.kawapp.core.database.model.BrewLogEntity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Access to the brew_logs table. */
@Dao
interface BrewLogDao {
    /** All brew logs, newest first. Emits again whenever the table changes. */
    @Query("SELECT * FROM brew_logs ORDER BY date DESC, time DESC")
    fun getAllBrewLogs(): Flow<List<BrewLogEntity>>

    /** The brew log with [id], or null if it does not exist. */
    @Query("SELECT * FROM brew_logs WHERE id = :id ORDER BY date DESC, time DESC")
    fun getBrewLogById(id: String): Flow<BrewLogEntity?>

    /** Every date that has at least one brew log (used for the calendar dots and the streak). */
    @Query("SELECT DISTINCT date FROM brew_logs")
    fun getDatesWithBrews(): Flow<List<LocalDate>>

    /** Brew logs from [date], latest first. */
    @Query("SELECT * FROM brew_logs WHERE date = :date ORDER BY date DESC, time DESC")
    fun getBrewLogsForDate(date: LocalDate): Flow<List<BrewLogEntity>>

    /** Inserts [brewLog], replacing an existing log with the same id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrewLog(brewLog: BrewLogEntity)

    /** Deletes the brew log with [id]. Does nothing if it does not exist. */
    @Query("DELETE FROM brew_logs WHERE id = :id")
    suspend fun deleteBrewLog(id: String)
}