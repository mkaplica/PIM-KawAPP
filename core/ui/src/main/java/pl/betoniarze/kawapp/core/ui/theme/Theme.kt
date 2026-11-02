package pl.betoniarze.kawapp.core.ui.theme

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = CoffeeEspresso,
    onPrimary = CoffeeOnEspresso,
    primaryContainer = CoffeeEspressoContainer,
    onPrimaryContainer = CoffeeOnEspressoContainer,
    secondary = CaramelBrown,
    onSecondary = OnCaramel,
    secondaryContainer = CaramelContainer,
    onSecondaryContainer = OnCaramelContainer,
    tertiary = CinnamonAccent,
    onTertiary = OnCinnamon,
    tertiaryContainer = CinnamonContainer,
    onTertiaryContainer = OnCinnamonContainer,
    background = WarmBeigeBackground,
    onBackground = WarmOnSurface,
    surface = WarmBeigeSurface,
    onSurface = WarmOnSurface,
    surfaceVariant = WarmBeigeSurfaceVariant,
    onSurfaceVariant = WarmOnSurfaceVariant,
    outline = WarmOutline
)

private val DarkColorScheme = darkColorScheme(
    primary = DarkCoffeePrimary,
    onPrimary = DarkCoffeeOnPrimary,
    primaryContainer = DarkCoffeePrimaryContainer,
    onPrimaryContainer = DarkCoffeeOnPrimaryContainer,
    secondary = DarkCaramelSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkCaramelContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkCinnamonTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkCinnamonContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkCoffeeBackground,
    onBackground = DarkOnSurface,
    surface = DarkCoffeeSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkCoffeeSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

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

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context.findActivity())?.window ?: return@SideEffect
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                window.isNavigationBarContrastEnforced = false
            }
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
