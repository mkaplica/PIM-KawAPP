package pl.betoniarze.kawapp.feature.journal

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.betoniarze.kawapp.core.data.repository.BrewLogRepository
import pl.betoniarze.kawapp.core.model.BrewLog

data class JournalUiState(
    val today: LocalDate = LocalDate.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val brewsForSelectedDate: List<BrewLog> = emptyList(),
    val datesWithBrews: Set<LocalDate> = emptySet(),
    val streakDays: Int = 0,
    val isLoading: Boolean = true,
)

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val brewLogRepository: BrewLogRepository
) : ViewModel() {

    // BUG FIX: wcześniej na sztywno LocalDate.of(2026, 11, 3).
    private val selectedDateFlow = MutableStateFlow(LocalDate.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<JournalUiState> = combine(
        selectedDateFlow,
        // Pair the brews with the date they were loaded for, so a newly selected day
        // shows the loading overlay instead of the previous day's brews.
        selectedDateFlow.flatMapLatest { date ->
            brewLogRepository.getBrewLogsForDate(date).map { brews -> date to brews }
        },
        brewLogRepository.getDatesWithBrews()
    ) { selectedDate, (loadedDate, brews), datesWithBrews ->
        val today = LocalDate.now()
        val isLoading = loadedDate != selectedDate
        JournalUiState(
            today = today,
            selectedDate = selectedDate,
            brewsForSelectedDate = if (isLoading) emptyList() else brews,
            isLoading = isLoading,
            datesWithBrews = datesWithBrews,
            streakDays = calculateStreak(datesWithBrews, today),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = JournalUiState()
    )

    fun onDateSelected(date: LocalDate) {
        selectedDateFlow.value = date
    }

    fun onTodayClick() {
        selectedDateFlow.value = LocalDate.now()
    }
}

/**
 * Liczba kolejnych dni z parzeniem. Jeśli dziś jeszcze nic nie zapisano,
 * seria liczy się od wczoraj (nie przepada rano).
 */
internal fun calculateStreak(dates: Set<LocalDate>, today: LocalDate): Int {
    var day = if (today in dates) today else today.minusDays(1)
    var count = 0
    while (day in dates) {
        count++
        day = day.minusDays(1)
    }
    return count
}
