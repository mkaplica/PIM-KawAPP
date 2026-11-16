package pl.betoniarze.kawapp.feature.recipes.brew

import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.model.BrewStep
import kotlin.math.ceil

object TimeRangeParser {
    private val separator = Regex("\\s*[-–—]\\s*")

    fun parseSeconds(text: String): Int? {
        val parts = text.trim().split(":")
        return when (parts.size) {
            1 -> parts[0].toIntOrNull()
            2 -> {
                val m = parts[0].toIntOrNull()
                val s = parts[1].toIntOrNull()
                if (m == null || s == null) null else m * 60 + s
            }
            else -> null
        }
    }

    fun parse(range: String): Pair<Int?, Int?> {
        val parts = range.split(separator).filter { it.isNotBlank() }
        return parts.getOrNull(0)?.let(::parseSeconds) to parts.getOrNull(1)?.let(::parseSeconds)
    }
}

data class TimedStep(
    val step: BrewStep,
    val startSec: Int,
    val endSec: Int,
) {
    val durationSec: Int get() = (endSec - startSec).coerceAtLeast(1)
}

private const val DEFAULT_LAST_STEP_SEC = 30

fun CoffeeRecipe.toTimedSteps(): List<TimedStep> {
    val parsed = steps.mapNotNull { step ->
        val (start, end) = TimeRangeParser.parse(step.timeRange)
        start?.let { Triple(step, it, end) }
    }.sortedBy { it.second }

    return parsed.mapIndexed { i, (step, start, end) ->
        val nextStart = parsed.getOrNull(i + 1)?.second
        val resolvedEnd = end ?: nextStart ?: (start + DEFAULT_LAST_STEP_SEC)
        TimedStep(step, start, resolvedEnd.coerceAtLeast(start + 1))
    }
}

data class BrewTimerState(
    val steps: List<TimedStep> = emptyList(),
    val elapsedMs: Long = 0L,
    val isRunning: Boolean = false,
) {
    val totalMs: Long get() = (steps.maxOfOrNull { it.endSec } ?: 0) * 1000L
    val hasSteps: Boolean get() = steps.isNotEmpty()
    val isFinished: Boolean get() = hasSteps && elapsedMs >= totalMs
    val isIdle: Boolean get() = elapsedMs == 0L && !isRunning

    val currentStepIndex: Int
        get() = if (isFinished) {
            steps.size
        } else {
            steps.indexOfLast { elapsedMs >= it.startSec * 1000L }.coerceAtLeast(0)
        }

    val currentStep: TimedStep? get() = steps.getOrNull(currentStepIndex)
    val nextStep: TimedStep? get() = steps.getOrNull(currentStepIndex + 1)

    val stepProgress: Float
        get() = currentStep?.let { s ->
            ((elapsedMs - s.startSec * 1000f) / (s.durationSec * 1000f)).coerceIn(0f, 1f)
        } ?: 1f

    val secondsLeftInStep: Int? get() = currentStep?.let { secondsUntil(it.endSec) }

    private fun secondsUntil(sec: Int): Int =
        ceil((sec * 1000L - elapsedMs) / 1000.0).toInt().coerceAtLeast(0)
}
