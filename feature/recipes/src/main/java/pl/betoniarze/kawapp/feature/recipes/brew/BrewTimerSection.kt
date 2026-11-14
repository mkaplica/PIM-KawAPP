package pl.betoniarze.kawapp.feature.recipes.brew

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.feature.recipes.brew.components.TimerControls

@Composable
fun BrewTimerSection(
    recipe: CoffeeRecipe,
    vm: BrewTimerViewModel,
    modifier: Modifier = Modifier,
    showControls: Boolean = false,
) {
    LaunchedEffect(recipe.id) { vm.bind(recipe) }
    val state by vm.state.collectAsStateWithLifecycle()

    KeepScreenOn(enabled = state.isRunning)
    StepChangeFeedback(state)

    if (showControls) {
        TimerControls(
            state = state,
            onToggle = vm::toggle,
            onReset = vm::reset,
            onSkip = vm::skipToNextStep,
            modifier = modifier,
        )
    }
}

@Composable
private fun KeepScreenOn(enabled: Boolean) {
    val view = LocalView.current
    DisposableEffect(enabled) {
        view.keepScreenOn = enabled
        onDispose { view.keepScreenOn = false }
    }
}

@Composable
private fun StepChangeFeedback(state: BrewTimerState) {
    val haptic = LocalHapticFeedback.current
    val tone = remember { runCatching { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80) }.getOrNull() }
    DisposableEffect(Unit) { onDispose { tone?.release() } }

    var lastAnnounced by rememberSaveable { mutableIntStateOf(state.currentStepIndex) }
    val index = state.currentStepIndex

    LaunchedEffect(index) {
        if (index > lastAnnounced && index > 0) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            tone?.startTone(ToneGenerator.TONE_PROP_BEEP, 200)
        }
        lastAnnounced = index
    }
}