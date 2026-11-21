package pl.betoniarze.kawapp.feature.recipes.brew.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.theme.KawTheme
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerState

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

/** Przyciski timera. Pauza/start w karmelu (timerAccent), reszta outlined. */
@Composable
fun TimerControls(
    state: BrewTimerState,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val kaw = KawTheme.colors
    val outline = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    val outlinedColors = ButtonDefaults.outlinedButtonColors(
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OutlinedButton(
            onClick = onReset,
            enabled = !state.isIdle,
            border = outline,
            colors = outlinedColors,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
        ) {
            Icon(Icons.Filled.Refresh, contentDescription = stringResource(R.string.reset_timer))
        }

        Button(
            onClick = onToggle,
            enabled = state.hasSteps && !state.isFinished,
            colors = ButtonDefaults.buttonColors(
                containerColor = kaw.timerAccent,
                contentColor = kaw.onTimerAccent,
            ),
            modifier = Modifier
                .weight(1.4f)
                .height(48.dp),
        ) {
            Icon(
                imageVector = if (state.isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                contentDescription = null,
            )
            Spacer(Modifier.width(8.dp))
            Text(stringResource(if (state.isRunning) R.string.pause_timer else R.string.resume_timer))
        }

        OutlinedButton(
            onClick = onSkip,
            enabled = !state.isIdle && state.nextStep != null,
            border = outline,
            colors = outlinedColors,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
        ) {
            Icon(Icons.Filled.SkipNext, contentDescription = stringResource(R.string.next_step))
        }
    }
}
