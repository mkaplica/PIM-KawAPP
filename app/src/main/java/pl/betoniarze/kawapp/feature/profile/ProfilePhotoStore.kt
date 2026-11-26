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
 * Saves the profile photo as a file:// URI (see [pl.betoniarze.kawapp.ui.photos.PhotoStorage]),
 * in the same SharedPreferences file as the other settings.
 */
@Singleton
class ProfilePhotoStore @Inject constructor(
    @ApplicationContext context: Context,
) {
    private val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _photoUri = MutableStateFlow(prefs.getString(KEY_PHOTO_URI, null).orEmpty())
    /** The current profile photo, empty when the user has none (the monogram is shown). */
    val photoUri: StateFlow<String> = _photoUri.asStateFlow()

    /** Saves [uri]; an empty string removes the photo. */
    fun setPhotoUri(uri: String) {
        prefs.edit { putString(KEY_PHOTO_URI, uri) }
        _photoUri.value = uri
    }

    private companion object {
        const val PREFS_NAME = "app_settings"
        const val KEY_PHOTO_URI = "profile_photo_uri"
    }
}
