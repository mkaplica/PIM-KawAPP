package pl.betoniarze.kawapp.feature.profile

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Saves the user name set in the Profile, in the same SharedPreferences file as the other app
 * settings. Exposed as a [StateFlow], so the Feed greeting updates as soon as it changes.
 */
@Singleton
class UserNameStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _userName = MutableStateFlow(prefs.getString(KEY_USER_NAME, null).orEmpty())
    /** The saved name, empty until the user sets one. */
    val userName: StateFlow<String> = _userName.asStateFlow()

    /** Saves [name] trimmed; a blank name clears it. */
    fun setUserName(name: String) {
        val trimmed = name.trim()
        prefs.edit { putString(KEY_USER_NAME, trimmed) }
        _userName.value = trimmed
    }

    /** Name used as the author of the user's posts: the saved name, or [FALLBACK_AUTHOR]. */
    val authorName: String
        get() = _userName.value.ifBlank { FALLBACK_AUTHOR }

    companion object {
        /** Shown as the post author until the user sets a name. */
        const val FALLBACK_AUTHOR = "Barista"

        private const val PREFS_NAME = "app_settings"
        private const val KEY_USER_NAME = "user_name"
    }
}
