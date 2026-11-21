package pl.betoniarze.kawapp.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Kolory KawApp, których nie ma w Material [androidx.compose.material3.ColorScheme].
 *
 * Tag (chip metody, streak, Kawosy) jest JEDNYM komponentem typu outlined/tonal:
 * ciemniejsze tło + kolorowy tekst + obwódka w tym samym kolorze.
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

val LocalKawColors = staticCompositionLocalOf { LightKawColors }

/** Dostęp: `KawTheme.colors.card` */
object KawTheme {
    val colors: KawColors
        @Composable
        @ReadOnlyComposable
        get() = LocalKawColors.current
}
