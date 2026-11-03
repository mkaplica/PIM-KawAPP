package pl.betoniarze.kawapp.feature.journal.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.TemporalAdjusters
import java.util.Locale

private const val INITIAL_PAGE = 500
private const val TOTAL_PAGES = 1000

@Composable
internal fun WeeklyCalendarStrip(
    selectedDate: LocalDate,
    datesWithBrews: Set<LocalDate>,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val polishLocale = remember { Locale("pl", "PL") }
    val baseMonday = remember {
        LocalDate.of(2026, 11, 3).with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    }

    val pagerState = rememberPagerState(
        initialPage = INITIAL_PAGE,
        pageCount = { TOTAL_PAGES }
    )

    val visibleWeekMonday = remember(pagerState.currentPage) {
        baseMonday.plusWeeks((pagerState.currentPage - INITIAL_PAGE).toLong())
    }
    val monthHeader = remember(visibleWeekMonday) {
        val monthName = visibleWeekMonday.month.getDisplayName(TextStyle.FULL_STANDALONE, polishLocale)
            .replaceFirstChar { it.uppercase() }
        "$monthName ${visibleWeekMonday.year}"
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = monthHeader,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val weekOffset = (page - INITIAL_PAGE).toLong()
                val weekStartMonday = baseMonday.plusWeeks(weekOffset)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (dayIndex in 0..6) {
                        val date = weekStartMonday.plusDays(dayIndex.toLong())
                        val isSelected = date == selectedDate
                        val hasBrews = datesWithBrews.contains(date)

                        DayItem(
                            date = date,
                            isSelected = isSelected,
                            hasBrews = hasBrews,
                            locale = polishLocale,
                            onClick = { onDateSelected(date) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    hasBrews: Boolean,
    locale: Locale,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.surface
    }

    val contentColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else {
        MaterialTheme.colorScheme.onSurface
    }

    val dayName = date.dayOfWeek
        .getDisplayName(TextStyle.SHORT_STANDALONE, locale)
        .take(3)
        .replaceFirstChar { it.uppercase() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(horizontal = 2.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(containerColor)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) contentColor else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = contentColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(
                    when {
                        hasBrews && isSelected -> MaterialTheme.colorScheme.onPrimary
                        hasBrews && !isSelected -> MaterialTheme.colorScheme.primary
                        else -> containerColor
                    }
                )
        )
    }
}
