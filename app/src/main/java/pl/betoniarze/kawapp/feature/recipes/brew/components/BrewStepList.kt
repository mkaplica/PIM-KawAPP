package pl.betoniarze.kawapp.feature.recipes.brew.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.model.BrewStep
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.theme.KawTheme
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerState

/** Where a step is relative to the timer. */
private enum class BrewStepStatus { DONE, ACTIVE, UPCOMING }

/**
 * Recipe steps as a timeline. Active step = full honey (the only accent on this screen),
 * completed = caramel (timerAccent), upcoming = outlineVariant border.
 */
@Composable
fun BrewStepsList(
    steps: List<BrewStep>,
    timerState: BrewTimerState,
    modifier: Modifier = Modifier,
) {
    val active = !timerState.isIdle
    val currentNumber = timerState.currentStep?.step?.stepNumber
    val doneNumbers = remember(timerState.currentStepIndex, timerState.steps) {
        timerState.steps.take(timerState.currentStepIndex).map { it.step.stepNumber }.toSet()
    }
    val sorted = remember(steps) { steps.sortedBy { it.stepNumber } }

    Column(modifier) {
        sorted.forEachIndexed { index, step ->
            val status = when {
                !active -> BrewStepStatus.UPCOMING
                step.stepNumber == currentNumber && !timerState.isFinished -> BrewStepStatus.ACTIVE
                step.stepNumber in doneNumbers -> BrewStepStatus.DONE
                else -> BrewStepStatus.UPCOMING
            }
            BrewStepRow(
                step = step,
                status = status,
                isLast = index == sorted.lastIndex,
                progress = if (status == BrewStepStatus.ACTIVE) timerState.stepProgress else 0f,
            )
        }
        AnimatedVisibility(visible = timerState.isFinished) {
            BrewFinishedMessage(Modifier.padding(top = 8.dp))
        }
    }
}

/**
 * One step in the timeline: marker, description, time range and water. The active step is a honey
 * card with a progress bar.
 */
@Composable
private fun BrewStepRow(
    step: BrewStep,
    status: BrewStepStatus,
    isLast: Boolean,
    progress: Float,
) {
    val colors = MaterialTheme.colorScheme
    val kaw = KawTheme.colors
    val lineColor by animateColorAsState(
        if (status == BrewStepStatus.DONE) kaw.timerAccent else colors.outlineVariant,
        label = "stepLine",
    )

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxHeight()) {
            StepMarker(number = step.stepNumber, status = status)
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(lineColor)
                )
            }
        }

        val meta = buildString {
            if (step.timeRange.isNotBlank()) append(step.timeRange)
            if (step.waterAmountMl > 0) {
                if (isNotEmpty()) append(" · ")
                append("${step.waterAmountMl} ml")
            }
        }

        if (status == BrewStepStatus.ACTIVE) {
            Surface(
                color = colors.primary,
                contentColor = colors.onPrimary,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 10.dp),
            ) {
                Column(Modifier.padding(horizontal = 10.dp, vertical = 8.dp)) {
                    Text(step.description, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                    if (meta.isNotEmpty()) Text(meta, style = MaterialTheme.typography.labelSmall)
                    Spacer(Modifier.height(6.dp))
                    BrewProgressBar(
                        progress = progress,
                        height = 4.dp,
                        color = colors.onPrimary,
                        trackColor = colors.onPrimary.copy(alpha = 0.2f),
                    )
                }
            }
        } else {
            val textColor = if (status == BrewStepStatus.DONE) colors.onSurfaceVariant else colors.onSurface
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 12.dp)
            ) {
                Text(step.description, style = MaterialTheme.typography.bodyMedium, color = textColor)
                if (meta.isNotEmpty()) {
                    Text(meta, style = MaterialTheme.typography.labelSmall, color = colors.onSurfaceVariant)
                }
            }
        }
    }
}

/**
 * Step number in a circle: honey when active, caramel with a check when done, outlined when
 * upcoming.
 */
@Composable
private fun StepMarker(number: Int, status: BrewStepStatus) {
    val colors = MaterialTheme.colorScheme
    val kaw = KawTheme.colors
    val (container, content, border) = when (status) {
        BrewStepStatus.DONE -> Triple(kaw.timerAccent, kaw.onTimerAccent, kaw.timerAccent)
        BrewStepStatus.ACTIVE -> Triple(colors.primary, colors.onPrimary, colors.primary)
        BrewStepStatus.UPCOMING -> Triple(Color.Transparent, colors.onSurfaceVariant, colors.outlineVariant)
    }
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(container)
            .border(BorderStroke(2.dp, border), CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (status == BrewStepStatus.DONE) {
            Icon(Icons.Filled.Check, contentDescription = null, tint = content, modifier = Modifier.size(14.dp))
        } else {
            Text(
                text = number.toString(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (status == BrewStepStatus.ACTIVE) FontWeight.Bold else null,
                color = content,
            )
        }
    }
}

/** Message shown under the steps when the brew is finished. */
@Composable
private fun BrewFinishedMessage(modifier: Modifier = Modifier) {
    val kaw = KawTheme.colors
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(kaw.tagContainer)
            .border(BorderStroke(1.5.dp, kaw.tagBorder), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = stringResource(R.string.brew_finished),
            style = MaterialTheme.typography.titleLarge,
            color = kaw.onTag,
        )
        Text(
            text = stringResource(R.string.brew_enjoy_coffee_message),
            style = MaterialTheme.typography.bodyLarge,
            color = kaw.onTag,
        )
    }
}
