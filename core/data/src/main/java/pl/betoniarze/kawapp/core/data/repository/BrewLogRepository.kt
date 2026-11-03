package pl.betoniarze.kawapp.core.data.repository

import pl.betoniarze.kawapp.core.model.BrewLog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface BrewLogRepository {
    fun getAllBrewLogs(): Flow<List<BrewLog>>
    fun getBrewLogsForDate(date: LocalDate): Flow<List<BrewLog>>
    fun getDatesWithBrews(): Flow<Set<LocalDate>>
}