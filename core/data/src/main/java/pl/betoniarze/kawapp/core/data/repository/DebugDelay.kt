package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import pl.betoniarze.kawapp.core.data.BuildConfig
import kotlin.time.Duration.Companion.milliseconds

/**
 * Artificial delay added to repository flows in debug builds, so the loading animation can be
 * tested.
 */
internal const val DEBUG_LOAD_DELAY_MS = 1_500L

/**
 * Debug builds: delays the first emission by [DEBUG_LOAD_DELAY_MS]. Release builds: returns the
 * flow unchanged.
 */
internal fun <T> Flow<T>.withDebugDelay(): Flow<T> =
    if (BuildConfig.DEBUG) onStart { delay(DEBUG_LOAD_DELAY_MS.milliseconds) } else this
