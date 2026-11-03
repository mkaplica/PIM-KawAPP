package pl.betoniarze.kawapp.feature.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.betoniarze.kawapp.feature.journal.components.BrewLogSummaryCard
import pl.betoniarze.kawapp.feature.journal.components.WeeklyCalendarStrip
import java.time.LocalDate

@Composable
fun JournalScreen(
    modifier: Modifier = Modifier,
    onBrewClick: (String) -> Unit = {},
    viewModel: JournalViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    JournalScreen(
        uiState = uiState,
        onDateSelected = viewModel::onDateSelected,
        onBrewClick = onBrewClick,
        modifier = modifier
    )
}

@Composable
fun JournalScreen(
    uiState: JournalUiState,
    onDateSelected: (LocalDate) -> Unit,
    onBrewClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        WeeklyCalendarStrip(
            selectedDate = uiState.selectedDate,
            datesWithBrews = uiState.datesWithBrews,
            onDateSelected = onDateSelected
        )

        if (uiState.brewsForSelectedDate.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Brak zapisanych parzeń tego dnia",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = uiState.brewsForSelectedDate,
                    key = { it.id }
                ) { brewLog ->
                    BrewLogSummaryCard(
                        brewLog = brewLog,
                        onClick = { onBrewClick(brewLog.id) }
                    )
                }
            }
        }
    }
}

