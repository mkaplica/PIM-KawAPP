package pl.betoniarze.kawapp.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * KawApp colors that the Material [androidx.compose.material3.ColorScheme] has no slot for.
 *
 * The tag (method chip, streak, Kawos) is ONE outlined/tonal component:
 * a darker background + colored text + a border in the same color.
 *
 * @property card Background of [pl.betoniarze.kawapp.core.ui.components.KawCard].
 * @property brewDot Dot under calendar days that have a brew.
 * @property timerAccent Caramel used for the brew timer's pause button and completed steps.
 */
@Immutable
data class KawColors(
    val card: Color,
    val cardBorder: Color,
    val tagContainer: Color,
    val onTag: Color,
    val tagBorder: Color,
    val timerAccent: Color,
    val onTimerAccent: Color,
    val brewDot: Color,
)

/** [KawColors] for the light theme. */
val LightKawColors = KawColors(
    card = LightCardColor,
    cardBorder = LightCardBorder,
    tagContainer = LightTertiaryContainer,
    onTag = LightOnTertiaryContainer,
    tagBorder = LightTagBorder,
    timerAccent = LightTimerAccent,
    onTimerAccent = LightOnTimerAccent,
    brewDot = LightFirstDetailColor,
)

/** [KawColors] for the dark theme. */
val DarkKawColors = KawColors(
    card = DarkCardColor,
    cardBorder = DarkCardBorder,
    tagContainer = DarkTertiaryContainer,
    onTag = DarkOnTertiaryContainer,
    tagBorder = DarkTagBorder,
    timerAccent = DarkTimerAccent,
    onTimerAccent = DarkOnTimerAccent,
    brewDot = DarkFirstDetailColor,
)

/** Provides [KawColors] down the tree; set by KawAPPTheme. Read it through [KawTheme.colors]. */
val LocalKawColors = staticCompositionLocalOf { LightKawColors }

/** Access to the current [KawColors], e.g. `KawTheme.colors.card`. */
object KawTheme {
    val colors: KawColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKawColors.current
}
