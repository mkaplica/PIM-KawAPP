package pl.betoniarze.kawapp.core.data.repository

import pl.betoniarze.kawapp.core.model.BrewLog
import pl.betoniarze.kawapp.core.data.fake.FakeCoffeeData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class FakeBrewLogRepository : BrewLogRepository {
    private val logsFlow = MutableStateFlow(FakeCoffeeData.brewLogs)

    override fun getAllBrewLogs(): Flow<List<BrewLog>> = logsFlow.asStateFlow()

    override fun getBrewLogsForDate(date: LocalDate): Flow<List<BrewLog>> =
        logsFlow.map { logs -> logs.filter { it.date == date } }

    override fun getDatesWithBrews(): Flow<Set<LocalDate>> =
        logsFlow.map { logs -> logs.map { it.date }.toSet() }
}