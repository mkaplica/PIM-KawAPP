package pl.betoniarze.kawapp.feature.recipes.brew

import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.model.BrewStep
import kotlin.math.ceil

/** Parses step time ranges such as "0:30 - 1:15" into seconds. */
object TimeRangeParser {
    private val separator = Regex("\\s*[-–—]\\s*")

    /** Parses "m:ss" or plain seconds ("45"); null if [text] is not a valid time. */
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

    /**
     * Parses a range into its (start, end) seconds. Either part is null if missing or invalid;
     * hyphen, en dash and em dash all work as separators.
     */
    fun parse(range: String): Pair<Int?, Int?> {
        val parts = range.split(separator).filter { it.isNotBlank() }
        return parts.getOrNull(0)?.let(::parseSeconds) to parts.getOrNull(1)?.let(::parseSeconds)
    }
}

/** A recipe step placed on the timer timeline, in seconds from the brew start. */
data class TimedStep(
    val step: BrewStep,
    val startSec: Int,
    val endSec: Int,
) {
    /** Step length in seconds, at least 1. */
    val durationSec: Int get() = (endSec - startSec).coerceAtLeast(1)
}

/** Length given to the last step when its range has no end time. */
private const val DEFAULT_LAST_STEP_SEC = 30

/**
 * Turns the recipe steps into a sorted timeline. Steps without a valid start time are skipped. A
 * missing end time is taken from the next step's start, or [DEFAULT_LAST_STEP_SEC] for the last
 * step.
 */
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

/**
 * Snapshot of the brew timer.
 *
 * @property elapsedMs Time since the brew started, in milliseconds.
 */
data class BrewTimerState(
    val steps: List<TimedStep> = emptyList(),
    val elapsedMs: Long = 0L,
    val isRunning: Boolean = false,
) {
    val totalMs: Long get() = (steps.maxOfOrNull { it.endSec } ?: 0) * 1000L
    val hasSteps: Boolean get() = steps.isNotEmpty()
    val isFinished: Boolean get() = hasSteps && elapsedMs >= totalMs
    val isIdle: Boolean get() = elapsedMs == 0L && !isRunning

    /** Index of the step in progress; equals steps.size once the brew is finished. */
    val currentStepIndex: Int
        get() = if (isFinished) {
            steps.size
        } else {
            steps.indexOfLast { elapsedMs >= it.startSec * 1000L }.coerceAtLeast(0)
        }

    val currentStep: TimedStep? get() = steps.getOrNull(currentStepIndex)
    val nextStep: TimedStep? get() = steps.getOrNull(currentStepIndex + 1)

    /** True once the brew has started and is not finished; skipping the last step finishes it. */
    val canSkip: Boolean get() = !isIdle && !isFinished

    /** Progress of the current step from 0 to 1; 1 when finished. */
    val stepProgress: Float
        get() = currentStep?.let { s ->
            ((elapsedMs - s.startSec * 1000f) / (s.durationSec * 1000f)).coerceIn(0f, 1f)
        } ?: 1f

    /** Whole seconds left in the current step (rounded up), or null when finished. */
    val secondsLeftInStep: Int? get() = currentStep?.let { secondsUntil(it.endSec) }

    /** Whole seconds (rounded up) until second [sec] of the brew, at least 0. */
    private fun secondsUntil(sec: Int): Int =
        ceil((sec * 1000L - elapsedMs) / 1000.0).toInt().coerceAtLeast(0)
}
