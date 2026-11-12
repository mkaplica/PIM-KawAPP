package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.database.dao.BrewLogDao
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.BrewLog
import java.time.LocalDate

class LocalBrewLogRepository (
    private val brewLogDao: BrewLogDao
) : BrewLogRepository {
    override fun getAllBrewLogs(): Flow<List<BrewLog>> =
        brewLogDao.getAllBrewLogs()
            .map{ list -> list.map { it.asExternalModel() } }

    override fun getBrewLogsForDate(date: LocalDate): Flow<List<BrewLog>> =
        brewLogDao.getBrewLogsForDate(date)
            .map { list -> list.map { it.asExternalModel() } }

    override fun getDatesWithBrews(): Flow<Set<LocalDate>> =
        brewLogDao.getDatesWithBrews()
            .map { it.toSet() }
}