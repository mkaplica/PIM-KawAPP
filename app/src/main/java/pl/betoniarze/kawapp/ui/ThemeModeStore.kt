package pl.betoniarze.kawapp.ui

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import pl.betoniarze.kawapp.core.model.ThemeMode

/**
 * Saves the chosen theme mode and applies it to the whole app through AppCompat.
 * AppCompat does not remember the night mode after the process restarts (unlike the
 * language, which autoStoreLocales persists), so it is stored in SharedPreferences.
 */
@Singleton
class ThemeModeStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** The saved mode, or [ThemeMode.SYSTEM] if nothing was saved yet. */
    val themeMode: ThemeMode
        get() = ThemeMode.fromName(prefs.getString(KEY_THEME_MODE, null))

    /**
     * Saves [mode] and applies it. AppCompat recreates the running Activities so the new theme
     * shows immediately.
     */
    fun setThemeMode(mode: ThemeMode) {
        prefs.edit { putString(KEY_THEME_MODE, mode.name) }
        apply(mode)
    }

    /** Applies the saved mode. Called in Application.onCreate, before the first Activity is created. */
    fun applySaved() = apply(themeMode)

    /** Maps [mode] to the AppCompat night mode setting. */
    private fun apply(mode: ThemeMode) {
        AppCompatDelegate.setDefaultNightMode(
            when (mode) {
                ThemeMode.SYSTEM -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                ThemeMode.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                ThemeMode.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }

    private companion object {
        const val PREFS_NAME = "app_settings"
        const val KEY_THEME_MODE = "theme_mode"
    }
}
