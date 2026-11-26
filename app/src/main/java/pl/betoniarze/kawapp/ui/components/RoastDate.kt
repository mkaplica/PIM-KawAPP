package pl.betoniarze.kawapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import pl.betoniarze.kawapp.core.ui.R

/** Corner radius shared by the outlined fields in the app's forms. */
val KawFieldShape = RoundedCornerShape(12.dp)

/** Formats a roast date the way forms and lists show it, e.g. "12 listopada 2026". */
@Composable
fun rememberRoastDateFormatter(): DateTimeFormatter {
    val locale = ConfigurationCompat.getLocales(LocalConfiguration.current)[0] ?: Locale.getDefault()
    return remember(locale) { DateTimeFormatter.ofPattern("d MMMM yyyy", locale) }
}

/**
 * Read-only field showing [date] that opens a date picker on tap. Future dates cannot be picked.
 * Owns the picker's visibility, so callers only handle the chosen date.
 */
@Composable
fun RoastDateField(
    date: LocalDate?,
    onDateChange: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showPicker by rememberSaveable { mutableStateOf(false) }
    val formatter = rememberRoastDateFormatter()

    if (showPicker) {
        RoastDatePickerDialog(
            initial = date,
            onConfirm = {
                onDateChange(it)
                showPicker = false
            },
            onDismiss = { showPicker = false },
        )
    }

    Box(modifier = modifier) {
        OutlinedTextField(
            value = date?.format(formatter).orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(text = stringResource(R.string.roast_date_label)) },
            trailingIcon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = null) },
            shape = KawFieldShape,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        // The text field would swallow taps for its own cursor, so a transparent layer opens the picker.
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 8.dp)
                .clip(KawFieldShape)
                .clickable(onClickLabel = stringResource(R.string.choose_date)) { showPicker = true },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoastDatePickerDialog(
    initial: LocalDate?,
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val today = remember { LocalDate.now() }
    val todayMillis = remember(today) { today.toUtcMillis() }
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initial?.toUtcMillis(),
        selectableDates = remember(todayMillis) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= todayMillis
                override fun isSelectableYear(year: Int) = year <= today.year
            }
        },
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { state.selectedDateMillis?.let { onConfirm(it.toUtcLocalDate()) } },
                enabled = state.selectedDateMillis != null,
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    ) {
        DatePicker(state = state)
    }
}

private fun LocalDate.toUtcMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toUtcLocalDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()
