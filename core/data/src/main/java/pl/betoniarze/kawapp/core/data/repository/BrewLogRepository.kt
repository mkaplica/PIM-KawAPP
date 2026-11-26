package pl.betoniarze.kawapp.core.data.repository

import pl.betoniarze.kawapp.core.model.BrewLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

/** Source of Journal brew logs. All flows emit again when the data changes. */
interface BrewLogRepository {
    /** All brew logs, newest first. */
    fun getAllBrewLogs(): Flow<List<BrewLog>>
    /** Brew logs from [date], latest first. */
    fun getBrewLogsForDate(date: LocalDate): Flow<List<BrewLog>>
    /** Every date that has at least one brew (calendar dots and streak). */
    fun getDatesWithBrews(): Flow<Set<LocalDate>>

    suspend fun saveBrewLog(brewLog: BrewLog)
}