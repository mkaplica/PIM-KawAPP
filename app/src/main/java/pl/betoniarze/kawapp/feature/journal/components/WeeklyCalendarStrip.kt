package pl.betoniarze.kawapp.feature.journal.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawStreakTag
import pl.betoniarze.kawapp.core.ui.theme.KawTheme

/** Pager page of the current week; lower pages are past weeks. */
private const val INITIAL_PAGE = 500
/** Total number of week pages (about 9.5 years back and forward). */
private const val TOTAL_PAGES = 1000

/**
 * Journal header: month + "Today" button, the streak (the only honey accent on the screen) and a
 * swipeable week strip. The whole block has the navbar color (surfaceContainer), a rounded bottom
 * and extends behind the status bar.
 *
 * @param today Date treated as today; later days are dimmed.
 * @param datesWithBrews Days that get a brew dot.
 * @param onTodayClick Called by the "Today" button, which also scrolls back to the current week.
 */
@Composable
internal fun WeeklyCalendarStrip(
    today: LocalDate,
    selectedDate: LocalDate,
    datesWithBrews: Set<LocalDate>,
    streakDays: Int,
    onDateSelected: (LocalDate) -> Unit,
    onTodayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val locale = ConfigurationCompat.getLocales(LocalConfiguration.current)[0] ?: Locale.getDefault()
    val baseMonday = remember(today) { today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)) }

    val pagerState = rememberPagerState(initialPage = INITIAL_PAGE, pageCount = { TOTAL_PAGES })
    val scope = rememberCoroutineScope()

    val visibleWeekMonday = baseMonday.plusWeeks((pagerState.currentPage - INITIAL_PAGE).toLong())
    val monthHeader = remember(visibleWeekMonday, locale) {
        val monthName = visibleWeekMonday.month.getDisplayName(TextStyle.FULL_STANDALONE, locale)
            .replaceFirstChar { it.uppercase() }
        "$monthName ${visibleWeekMonday.year}"
    }

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(top = 16.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = monthHeader,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
                    modifier = Modifier.weight(1f),
                )
                OutlinedButton(
                    onClick = {
                        onTodayClick()
                        scope.launch { pagerState.animateScrollToPage(INITIAL_PAGE) }
                    },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                ) {
                    Text(stringResource(R.string.today), color = MaterialTheme.colorScheme.onSurface)
                }
            }

            if (streakDays > 0) {
                KawStreakTag(
                    streakDays = streakDays,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                )
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { page ->
                val weekStartMonday = baseMonday.plusWeeks((page - INITIAL_PAGE).toLong())
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (dayIndex in 0..6) {
                        val date = weekStartMonday.plusDays(dayIndex.toLong())
                        DayItem(
                            date = date,
                            isSelected = date == selectedDate,
                            isToday = date == today,
                            isFuture = ChronoUnit.DAYS.between(today, date) > 0,
                            hasBrews = date in datesWithBrews,
                            locale = locale,
                            onClick = { onDateSelected(date) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

/**
 * One day in the week strip: short day name, day number and a brew dot.
 * The selected day is filled with honey; today has an outline; future days are dimmed.
 */
@Composable
private fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    isFuture: Boolean,
    hasBrews: Boolean,
    locale: Locale,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme
    val container = if (isSelected) colors.primary else Color.Transparent
    val content = when {
        isSelected -> colors.onPrimary
        isFuture -> colors.onSurfaceVariant
        else -> colors.onSurface
    }
    val dotColor = when {
        !hasBrews -> Color.Transparent
        isSelected -> colors.onPrimary
        else -> KawTheme.colors.brewDot
    }
    val shape = RoundedCornerShape(12.dp)

    val dayName = date.dayOfWeek
        .getDisplayName(TextStyle.SHORT_STANDALONE, locale)
        .take(3)
        .replaceFirstChar { it.uppercase() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(shape)
            .background(container)
            .then(
                if (isToday && !isSelected) Modifier.border(BorderStroke(2.dp, colors.outline), shape)
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = dayName,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) content else colors.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = content
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(dotColor)
        )
    }
}
