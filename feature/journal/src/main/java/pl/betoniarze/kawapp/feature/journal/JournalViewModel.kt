package pl.betoniarze.kawapp.feature.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import pl.betoniarze.kawapp.core.data.repository.BrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.FakeBrewLogRepository
import pl.betoniarze.kawapp.core.model.BrewLog
import java.time.LocalDate

data class JournalUiState(
    val selectedDate: LocalDate = LocalDate.of(2026, 11, 3),
    val brewsForSelectedDate: List<BrewLog> = emptyList(),
    val datesWithBrews: Set<LocalDate> = emptySet()
)

class JournalViewModel (
    private val brewLogRepository: BrewLogRepository = FakeBrewLogRepository()
) : ViewModel() {

    private val selectedDateFlow = MutableStateFlow(LocalDate.of(2026, 11, 3))

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<JournalUiState> = combine(
        selectedDateFlow,
        selectedDateFlow.flatMapLatest { date -> brewLogRepository.getBrewLogsForDate(date) },
        brewLogRepository.getDatesWithBrews()
    ) { selectedDate, brews, datesWithBrews ->
        JournalUiState(
            selectedDate = selectedDate,
            brewsForSelectedDate = brews,
            datesWithBrews = datesWithBrews
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = JournalUiState()
    )

    fun onDateSelected(date: LocalDate) {
        selectedDateFlow.value = date
    }
}