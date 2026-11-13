package pl.betoniarze.kawapp.feature.recipes.brew.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerState
import pl.betoniarze.kawapp.feature.recipes.brew.formatTime

@Composable
fun BrewProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.surfaceContainerHighest,
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        color = color,
        trackColor = trackColor,
        strokeCap = StrokeCap.Round,
        gapSize = 0.dp,
        drawStopIndicator = {},
    )
}

@Composable
fun TimerDisplay(state: BrewTimerState, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = formatTime(state.elapsedMs),
            style = MaterialTheme.typography.displayLarge.copy(fontFeatureSettings = "tnum"),
            fontWeight = FontWeight.Bold,
        )
        Text(
            formatTime(state.totalMs),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(8.dp))
        BrewProgressBar(
            progress = state.totalProgress,
            height = 10.dp,
        )
    }
}

@Composable
fun TimerControls(
    state: BrewTimerState,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedButton(onClick = onReset, enabled = !state.isIdle, modifier = Modifier.weight(1f)) {
            Text(stringResource(R.string.reset_timer))
        }
        Button(
            onClick = onToggle,
            enabled = state.hasSteps && !state.isFinished,
            modifier = Modifier.weight(2f),
        ) {
            Text(
                stringResource(
                    when {
                    state.isRunning -> R.string.pause_timer
                    state.isIdle -> R.string.start_timer
                    else -> R.string.resume_timer
                    }
                )
            )
        }
        OutlinedButton(
            onClick = onSkip,
            enabled = !state.isIdle && state.nextStep != null,
            modifier = Modifier.weight(1f),
        ) { Text(stringResource(R.string.force_next_step)) }
    }
}