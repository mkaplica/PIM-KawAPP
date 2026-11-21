package pl.betoniarze.kawapp.feature.recipes.brew

import android.os.SystemClock
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import kotlin.time.Duration.Companion.milliseconds

/**
 * Brew timer of the recipe detail screen. Time is measured with SystemClock.elapsedRealtime, so it
 * stays correct if ticks are delayed.
 */
@HiltViewModel
class BrewTimerViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(BrewTimerState())
    /** Current timer state, updated about every [TICK_MS] ms while running. */
    val state: StateFlow<BrewTimerState> = _state.asStateFlow()

    private var boundRecipe: CoffeeRecipe? = null
    private var ticker: Job? = null
    private var baseElapsedMs = 0L
    private var startedAtMs = 0L

    /**
     * Sets up the timer for [recipe] and resets it. Compares the whole recipe, not just its id, so
     * an edited recipe (same id, new steps) rebuilds the timer; Room re-emitting an identical
     * recipe is a no-op.
     */
    fun bind(recipe: CoffeeRecipe) {
        if (recipe == boundRecipe) return
        boundRecipe = recipe
        reset()
        _state.value = BrewTimerState(steps = recipe.toTimedSteps())
    }

    /** Pauses a running timer, otherwise starts it. */
    fun toggle() = if (_state.value.isRunning) pause() else start()

    /**
     * Starts or resumes the timer. Does nothing if it is already running, has no steps or is
     * finished. Stops by itself at the end of the last step.
     */
    fun start() {
        val s = _state.value
        if (ticker?.isActive == true || !s.hasSteps || s.isFinished) return
        startedAtMs = SystemClock.elapsedRealtime()
        _state.update { it.copy(isRunning = true) }

        ticker = viewModelScope.launch {
            while (isActive) {
                val total = _state.value.totalMs
                val elapsed = baseElapsedMs + (SystemClock.elapsedRealtime() - startedAtMs)
                if (elapsed >= total) {
                    baseElapsedMs = total
                    _state.update { it.copy(elapsedMs = total, isRunning = false) }
                    break
                }
                _state.update { it.copy(elapsedMs = elapsed) }
                delay(TICK_MS.milliseconds)
            }
        }
    }

    /** Pauses the timer and keeps the elapsed time. */
    fun pause() {
        if (!_state.value.isRunning) return
        ticker?.cancel()
        baseElapsedMs = (baseElapsedMs + SystemClock.elapsedRealtime() - startedAtMs)
            .coerceAtMost(_state.value.totalMs)
        _state.update { it.copy(elapsedMs = baseElapsedMs, isRunning = false) }
    }

    /** Stops the timer and goes back to 0:00. */
    fun reset() {
        ticker?.cancel()
        baseElapsedMs = 0L
        _state.update { it.copy(elapsedMs = 0L, isRunning = false) }
    }

    /** Jumps to the start of the next step, if there is one. */
    fun skipToNextStep() {
        val next = _state.value.nextStep ?: return
        baseElapsedMs = next.startSec * 1000L
        startedAtMs = SystemClock.elapsedRealtime()
        _state.update { it.copy(elapsedMs = baseElapsedMs) }
    }

    private companion object {
        /** Interval between UI updates while running. */
        const val TICK_MS = 50L
    }
}