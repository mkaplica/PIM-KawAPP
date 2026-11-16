package pl.betoniarze.kawapp.feature.recipes.brew.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.model.BrewStep
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerState

private enum class BrewStepStatus { DONE, ACTIVE, UPCOMING }

private const val NORMAL_FONT_SIZE = 14f
private const val ACTIVE_STEP_FONT_SIZE = 18f


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

    Column(modifier, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        steps.sortedBy { it.stepNumber }.forEach { step ->
            val status = when {
                !active -> BrewStepStatus.UPCOMING
                step.stepNumber == currentNumber -> BrewStepStatus.ACTIVE
                step.stepNumber in doneNumbers -> BrewStepStatus.DONE
                else -> BrewStepStatus.UPCOMING
            }
            val isActive = status == BrewStepStatus.ACTIVE
            BrewStepRow(
                step = step,
                status = status,
                progress = if (status == BrewStepStatus.ACTIVE) timerState.stepProgress else 0f,
                secondsLeft = if (isActive) timerState.secondsLeftInStep else null,
            )
        }
        AnimatedVisibility(visible = timerState.isFinished) {
            BrewFinishedMessage(Modifier.padding(top = 8.dp))
        }
    }
}

@Composable
private fun BrewFinishedMessage(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    Column(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(colors.primary.copy(alpha = 0.18f))
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = stringResource(R.string.brew_finished),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = colors.primary,
        )
        Text(
            text = stringResource(R.string.brew_enjoy_coffee_message),
            style = MaterialTheme.typography.bodyLarge,
            color = colors.primary,
        )
    }
}

@Composable
private fun BrewStepRow(
    step: BrewStep,
    status: BrewStepStatus,
    progress: Float,
    secondsLeft: Int?,
) {
    val colors = MaterialTheme.colorScheme
    val isActive = status == BrewStepStatus.ACTIVE

    val background by animateColorAsState(
        if (isActive) colors.primary.copy(alpha = 0.18f) else Color.Transparent,
        label = "stepBackground",
    )
    val textColor by animateColorAsState(
        when (status) {
            BrewStepStatus.ACTIVE -> colors.primary
            BrewStepStatus.DONE -> colors.onSurfaceVariant.copy(alpha = 0.6f)
            BrewStepStatus.UPCOMING -> colors.onSurface
        },
        label = "stepText",
    )

    val fontSize by animateFloatAsState(
        targetValue = if (isActive) ACTIVE_STEP_FONT_SIZE else NORMAL_FONT_SIZE,
        label = "stepFontSize",
    )

    Column(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(background)
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${step.stepNumber}. ")
                }
                withStyle(
                    SpanStyle(
                        fontStyle = if (isActive) FontStyle.Normal else FontStyle.Italic,
                        fontWeight = if (isActive) FontWeight.Bold else null,
                    )
                ) {
                    append(step.description)
                }
            },
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = fontSize.sp,
            ),
            color = textColor,
        )

        AnimatedVisibility(visible = isActive) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BrewProgressBar(
                    progress = progress,
                    height = 4.dp,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = "${secondsLeft ?: 0} s",
                    modifier = Modifier.padding(start = 12.dp),
                    style = MaterialTheme.typography.titleMedium.copy(fontFeatureSettings = "tnum"),
                    fontWeight = FontWeight.Bold,
                    color = colors.primary,
                )
            }
        }
    }
}
