package pl.betoniarze.kawapp.feature.profile

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.model.AppLanguage
import pl.betoniarze.kawapp.core.model.ThemeMode
import pl.betoniarze.kawapp.feature.chime_generator.ChimeTone
import pl.betoniarze.kawapp.feature.chime_generator.ChimeToneStore
import pl.betoniarze.kawapp.feature.chime_generator.playRepeaterChime
import pl.betoniarze.kawapp.ui.ThemeModeStore

/** How long the overlay stays after a language or theme change (debug and release builds). */
internal const val SETTINGS_CHANGE_DELAY_MS = 2_000L

/** Time for the overlay to fully appear before the Activity is recreated. */
private const val OVERLAY_COVER_MS = 600L

/**
 * ViewModel of the Profile: app language, theme mode and brew step chime. Applies language and
 * theme changes behind the loading overlay.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val themeModeStore: ThemeModeStore,
    private val chimeToneStore: ChimeToneStore,
) : ViewModel() {

    private val _selectedLanguage = MutableStateFlow(getCurrentAppLanguage())
    /** Language currently selected in the UI. */
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    private val _selectedThemeMode = MutableStateFlow(themeModeStore.themeMode)
    /** Theme mode currently selected in the UI. */
    val selectedThemeMode: StateFlow<ThemeMode> = _selectedThemeMode.asStateFlow()

    private val _selectedChimeTone = MutableStateFlow(chimeToneStore.chimeTone)
    /** Chime played on brew step changes, currently selected in the UI. */
    val selectedChimeTone: StateFlow<ChimeTone> = _selectedChimeTone.asStateFlow()

    private val _isApplyingSettings = MutableStateFlow(false)
    /** True while a change is being applied; the screen shows the loading overlay. */
    val isApplyingSettings: StateFlow<Boolean> = _isApplyingSettings.asStateFlow()

    /**
     * Switches the app to [language]. Ignored if it is already selected or another change is in
     * progress.
     */
    fun selectLanguage(language: AppLanguage) {
        if (_selectedLanguage.value == language || _isApplyingSettings.value) return
        _selectedLanguage.value = language
        applyBehindOverlay {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language.code))
        }
    }

    /**
     * Switches the app to [mode] and saves it. Ignored if it is already selected or another change
     * is in progress.
     */
    fun selectThemeMode(mode: ThemeMode) {
        if (_selectedThemeMode.value == mode || _isApplyingSettings.value) return
        _selectedThemeMode.value = mode
        applyBehindOverlay { themeModeStore.setThemeMode(mode) }
    }

    /**
     * Saves [tone] as the brew step chime and plays it as a preview. Needs no overlay, since
     * nothing is recreated.
     */
    fun selectChimeTone(tone: ChimeTone) {
        _selectedChimeTone.value = tone
        chimeToneStore.chimeTone = tone
        viewModelScope.launch { playRepeaterChime(tone) }
    }

    /**
     * Shows the overlay, applies [change] once the overlay covers the screen (the Activity is
     * recreated, while this ViewModel and coroutine survive), and hides it [SETTINGS_CHANGE_DELAY_MS]
     * after the tap.
     */
    private fun applyBehindOverlay(change: () -> Unit) {
        viewModelScope.launch {
            _isApplyingSettings.value = true
            delay(OVERLAY_COVER_MS)
            change()
            delay(SETTINGS_CHANGE_DELAY_MS - OVERLAY_COVER_MS)
            _isApplyingSettings.value = false
        }
    }

    /** Language set through AppCompat, or the device language when the app has none set. */
    private fun getCurrentAppLanguage(): AppLanguage {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (!currentLocales.isEmpty) {
            val tag = currentLocales[0]?.language
            if (!tag.isNullOrBlank()) {
                return AppLanguage.fromCode(tag)
            }
        }
        return AppLanguage.fromCode(Locale.getDefault().language)
    }
}
