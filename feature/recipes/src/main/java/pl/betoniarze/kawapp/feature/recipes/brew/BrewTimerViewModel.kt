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

@HiltViewModel
class BrewTimerViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(BrewTimerState())
    val state: StateFlow<BrewTimerState> = _state.asStateFlow()

    private var boundRecipeId: String? = null
    private var ticker: Job? = null
    private var baseElapsedMs = 0L
    private var startedAtMs = 0L

    fun bind(recipe: CoffeeRecipe) {
        if (recipe.id == boundRecipeId) return
        boundRecipeId = recipe.id
        reset()
        _state.value = BrewTimerState(steps = recipe.toTimedSteps())
    }

    fun toggle() = if (_state.value.isRunning) pause() else start()

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

    fun pause() {
        if (!_state.value.isRunning) return
        ticker?.cancel()
        baseElapsedMs = (baseElapsedMs + SystemClock.elapsedRealtime() - startedAtMs)
            .coerceAtMost(_state.value.totalMs)
        _state.update { it.copy(elapsedMs = baseElapsedMs, isRunning = false) }
    }

    fun reset() {
        ticker?.cancel()
        baseElapsedMs = 0L
        _state.update { it.copy(elapsedMs = 0L, isRunning = false) }
    }

    fun skipToNextStep() {
        val next = _state.value.nextStep ?: return
        baseElapsedMs = next.startSec * 1000L
        startedAtMs = SystemClock.elapsedRealtime()
        _state.update { it.copy(elapsedMs = baseElapsedMs) }
    }

    private companion object {
        const val TICK_MS = 50L
    }
}