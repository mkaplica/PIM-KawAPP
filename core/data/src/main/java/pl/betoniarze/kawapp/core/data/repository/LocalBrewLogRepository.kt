package pl.betoniarze.kawapp.core.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.data.BuildConfig
import pl.betoniarze.kawapp.core.data.fake.FakeCoffeeData
import pl.betoniarze.kawapp.core.database.dao.BrewLogDao
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.BrewLog
import java.time.LocalDate

/** [BrewLogRepository] backed by Room. Debug builds also include [FakeCoffeeData.brewLogs]. */
class LocalBrewLogRepository @Inject constructor(
    private val brewLogDao: BrewLogDao
) : BrewLogRepository {

    /**
     * Sample logs shown next to the user's own logs in debug builds. Release builds never include
     * them.
     */
    private val sampleLogs: List<BrewLog> =
        if (BuildConfig.DEBUG) FakeCoffeeData.brewLogs else emptyList()

    override fun getAllBrewLogs(): Flow<List<BrewLog>> =
        brewLogDao.getAllBrewLogs()
            .map { list -> (list.map { it.asExternalModel() } + sampleLogs).sortedNewestFirst() }
            .withDebugDelay()

    override fun getBrewLogsForDate(date: LocalDate): Flow<List<BrewLog>> =
        brewLogDao.getBrewLogsForDate(date)
            .map { list ->
                (list.map { it.asExternalModel() } + sampleLogs.filter { it.date == date })
                    .sortedNewestFirst()
            }
            .withDebugDelay()

    override fun getDatesWithBrews(): Flow<Set<LocalDate>> =
        brewLogDao.getDatesWithBrews()
            .map { it.toSet() + sampleLogs.map { log -> log.date } }
            .withDebugDelay()
}

/** Same order as the DAO queries: newest date first, then latest time. */
private fun List<BrewLog>.sortedNewestFirst(): List<BrewLog> =
    sortedWith(compareByDescending<BrewLog> { it.date }.thenByDescending { it.time })
