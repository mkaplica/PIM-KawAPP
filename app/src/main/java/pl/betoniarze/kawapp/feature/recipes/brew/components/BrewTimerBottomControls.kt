package pl.betoniarze.kawapp.feature.recipes.brew.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawFormat
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerState

/** Button that starts the brew timer on the recipe detail screen. */
@Composable
fun BrewStartFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        text = { Text(stringResource(R.string.start_timer)) },
        icon = { Icon(imageVector = Icons.Filled.LocalCafe, contentDescription = null) },
    )
}

/** Bottom bar while brewing: large elapsed / total time, "step X of Y" and the timer buttons. */
@Composable
fun BrewTimerBottomBar(
    state: BrewTimerState,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val elapsedSec = (state.elapsedMs / 1000).toInt()
    val totalSec = (state.totalMs / 1000).toInt()
    val stepNumber = (state.currentStepIndex + 1).coerceAtMost(state.steps.size)

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                Text(
                    text = buildAnnotatedString {
                        append(KawFormat.duration(elapsedSec))
                        withStyle(
                            SpanStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        ) {
                            append(" / ${KawFormat.duration(totalSec)}")
                        }
                    },
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Medium,
                        fontFeatureSettings = "tnum",
                    ),
                )
                if (!state.isFinished) {
                    Text(
                        text = stringResource(
                            R.string.timer_step_progress,
                            stepNumber,
                            state.steps.size,
                            state.secondsLeftInStep ?: 0,
                        ),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            TimerControls(
                state = state,
                onToggle = onToggle,
                onReset = onReset,
                onSkip = onSkip,
            )
        }
    }
}
