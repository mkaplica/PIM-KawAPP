package pl.betoniarze.kawapp.core.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = LightPrimaryComponent,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondaryComponent,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightFirstDetailColor,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    error = LightSecondDetailColor,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    background = LightBackgroundColor,
    onBackground = LightOnBackground,
    surface = LightBackgroundColor,
    onSurface = LightOnSurface,
    surfaceVariant = LightNavbarColor,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceContainerLow = LightNavbarColor,
    surfaceContainer = LightNavbarColor,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    surfaceBright = LightBackgroundColor,
    surfaceDim = LightSurfaceDim,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    inversePrimary = LightInversePrimary,
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkPrimaryComponent,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondaryComponent,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkFirstDetailColor,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    error = DarkSecondDetailColor,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    background = DarkBackgroundColor,
    onBackground = DarkOnBackground,
    surface = DarkBackgroundColor,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkNavbarColor,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceContainerLow = DarkNavbarColor,
    surfaceContainer = DarkNavbarColor,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    surfaceBright = DarkSurfaceBright,
    surfaceDim = DarkBackgroundColor,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    inversePrimary = DarkInversePrimary,
)

/** App-specific names for [ColorScheme] roles, used by older screens. */
val ColorScheme.backgroundColor: Color get() = background
val ColorScheme.navbarColor: Color get() = surfaceContainer
val ColorScheme.primaryComponent: Color get() = primary
val ColorScheme.secondaryComponent: Color get() = secondary
val ColorScheme.firstDetailColor: Color get() = tertiary
val ColorScheme.secondDetailColor: Color get() = error

/**
 * The app theme: KawApp light/dark color schemes, typography and [KawColors].
 *
 * @param darkTheme Whether to use the dark palette. Follows the app's theme mode (see
 * MainActivity).
 * @param dynamicColor Use Material You wallpaper colors on Android 12+. Off by default to keep the
 * brand colors.
 */
@Composable
fun KawAPPTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val kawColors = if (darkTheme) DarkKawColors else LightKawColors

    CompositionLocalProvider(LocalKawColors provides kawColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
