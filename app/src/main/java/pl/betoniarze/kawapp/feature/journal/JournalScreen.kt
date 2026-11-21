package pl.betoniarze.kawapp.feature.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawListDefaults
import pl.betoniarze.kawapp.feature.journal.components.BrewLogSummaryCard
import pl.betoniarze.kawapp.feature.journal.components.WeeklyCalendarStrip

@Composable
fun JournalScreen(
    modifier: Modifier = Modifier,
    onBrewClick: (String) -> Unit = {},
    onAddBrewClick: () -> Unit = {},
    viewModel: JournalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    JournalScreen(
        uiState = uiState,
        onDateSelected = viewModel::onDateSelected,
        onTodayClick = viewModel::onTodayClick,
        onBrewClick = onBrewClick,
        onAddBrewClick = onAddBrewClick,
        modifier = modifier
    )
}

@Composable
fun JournalScreen(
    uiState: JournalUiState,
    onDateSelected: (LocalDate) -> Unit,
    onBrewClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onTodayClick: () -> Unit = {},
    onAddBrewClick: () -> Unit = {},
) {
    val locale = ConfigurationCompat.getLocales(LocalConfiguration.current)[0] ?: Locale.getDefault()
    val dayFormatter = remember(locale) { DateTimeFormatter.ofPattern("EEEE, d MMMM", locale) }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Nagłówek w kolorze navbara, oddzielony od listy kolorem tła.
            WeeklyCalendarStrip(
                today = uiState.today,
                selectedDate = uiState.selectedDate,
                datesWithBrews = uiState.datesWithBrews,
                streakDays = uiState.streakDays,
                onDateSelected = onDateSelected,
                onTodayClick = onTodayClick,
            )

            Text(
                text = buildString {
                    append(uiState.selectedDate.format(dayFormatter).replaceFirstChar { it.uppercase() })
                    if (uiState.brewsForSelectedDate.isNotEmpty()) {
                        append(" · ")
                        append(
                            pluralStringResource(
                                R.plurals.brews_count,
                                uiState.brewsForSelectedDate.size,
                                uiState.brewsForSelectedDate.size,
                            )
                        )
                    }
                },
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = null),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 12.dp),
            )

            if (uiState.brewsForSelectedDate.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.journal_empty_day),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(KawListDefaults.ItemSpacing),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(items = uiState.brewsForSelectedDate, key = { it.id }) { brewLog ->
                        BrewLogSummaryCard(
                            brewLog = brewLog,
                            onClick = { onBrewClick(brewLog.id) }
                        )
                    }
                }
            }
        }

        ExtendedFloatingActionButton(
            onClick = onAddBrewClick,
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            icon = { Icon(Icons.Default.Add, contentDescription = null) },
            text = { Text(stringResource(R.string.save_brew)) },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        )
    }
}
