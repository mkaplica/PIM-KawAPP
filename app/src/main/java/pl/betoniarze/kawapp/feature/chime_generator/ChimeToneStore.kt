package pl.betoniarze.kawapp.feature.chime_generator

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Saves which [ChimeTone] plays on brew step changes, in SharedPreferences. */
@Singleton
class ChimeToneStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** The saved tone, or [ChimeTone.FIRST] if nothing (or an unknown value) was saved. */
    var chimeTone: ChimeTone
        get() = prefs.getString(KEY_CHIME_TONE, null)
            ?.let { name -> ChimeTone.entries.firstOrNull { it.name == name } }
            ?: ChimeTone.FIRST
        set(value) = prefs.edit { putString(KEY_CHIME_TONE, value.name) }

    private companion object {
        /** Shared with the theme setting, so all app settings live in one file. */
        const val PREFS_NAME = "app_settings"
        const val KEY_CHIME_TONE = "chime_tone"
    }
}
