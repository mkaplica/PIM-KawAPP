package pl.betoniarze.kawapp

import android.os.Build
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.DisposableEffect
import dagger.hilt.android.AndroidEntryPoint
import pl.betoniarze.kawapp.core.ui.theme.KawAPPTheme
import pl.betoniarze.kawapp.ui.AppScaffold

/**
 * The single Activity of the app. Hosts the Compose UI and keeps the system bars transparent and
 * edge-to-edge.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applySystemBars(darkTheme = null)

        setContent {
            // Reflects the app theme from Profile (AppCompat night mode), not only the system one.
            val darkTheme = isSystemInDarkTheme()

            // Re-apply on every theme change. After AppCompat recreates the Activity for a
            // theme switch, the system bars otherwise keep an opaque system color, and
            // SystemBarStyle.auto would pick icon colors from the system theme.
            DisposableEffect(darkTheme) {
                applySystemBars(darkTheme)
                onDispose {}
            }

            KawAPPTheme(darkTheme = darkTheme) {
                AppScaffold()
            }
        }
    }

    /**
     * Makes the status and navigation bars transparent and edge-to-edge.
     *
     * @param darkTheme Whether the app currently uses its dark theme; picks light or dark bar
     * icons. Null detects it from the Activity resources (used before Compose knows the theme).
     */
    private fun applySystemBars(darkTheme: Boolean?) {
        val style = if (darkTheme == null) {
            SystemBarStyle.auto(TRANSPARENT, TRANSPARENT)
        } else {
            SystemBarStyle.auto(TRANSPARENT, TRANSPARENT) { darkTheme }
        }
        enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    }

    private companion object {
        const val TRANSPARENT = android.graphics.Color.TRANSPARENT
    }
}
