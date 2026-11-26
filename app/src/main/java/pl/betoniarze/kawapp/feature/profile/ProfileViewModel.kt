package pl.betoniarze.kawapp.feature.profile

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.data.repository.BrewMethodRepository
import pl.betoniarze.kawapp.core.data.repository.CoffeeBagRepository
import pl.betoniarze.kawapp.core.data.repository.GrinderRepository
import pl.betoniarze.kawapp.core.model.AppLanguage
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.CoffeeBag
import pl.betoniarze.kawapp.core.model.Grinder
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
 * ViewModel of the Profile: user name, the user's grinders and coffees, and the app settings
 * (language, theme, brew step chime). Applies language and theme changes behind the loading overlay.
 */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val themeModeStore: ThemeModeStore,
    private val chimeToneStore: ChimeToneStore,
    private val userNameStore: UserNameStore,
    private val grinderRepository: GrinderRepository,
    private val coffeeBagRepository: CoffeeBagRepository,
    private val brewMethodRepository: BrewMethodRepository
) : ViewModel() {

    /** Name shown in the header and used as the author of the user's posts; empty until set. */
    val userName: StateFlow<String> = userNameStore.userName

    val brewMethods: StateFlow<List<BrewMethod>?> = brewMethodRepository.getBrewMethods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)

    /** The user's grinders, oldest first; null until the first load, so no empty state flashes. */
    val grinders: StateFlow<List<Grinder>?> = grinderRepository.getGrinders()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)

    /** The user's coffees, newest first; null until the first load. */
    val coffeeBags: StateFlow<List<CoffeeBag>?> = coffeeBagRepository.getCoffeeBags()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), initialValue = null)

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

    /** Saves the user name; a blank name clears it. */
    fun saveUserName(name: String) {
        userNameStore.setUserName(name)
    }

    /** Adds a brew method called [name]. Blank names are ignored. */
    fun addBrewMethod(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            brewMethodRepository.addBrewMethod(BrewMethod(id = UUID.randomUUID().toString(), displayName = trimmed))
        }
    }

    /** Deletes [brewMethod]. Brew logs keep their copy; [restoreBrewMethod] undoes it. */
    fun deleteBrewMethod(brewMethod: BrewMethod) {
        viewModelScope.launch { brewMethodRepository.deleteBrewMethod(brewMethod.id) }
    }

    /** Puts back a grinder removed with [deleteGrinder] (the "Undo" action). */
    fun restoreBrewMethod(brewMethod: BrewMethod) {
        viewModelScope.launch { brewMethodRepository.addBrewMethod(brewMethod) }
    }

    /** Adds a grinder called [name]. Blank names are ignored. */
    fun addGrinder(name: String) {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return
        viewModelScope.launch {
            grinderRepository.addGrinder(Grinder(id = UUID.randomUUID().toString(), displayName = trimmed))
        }
    }

    /** Deletes [grinder]. Brew logs keep their copy; [restoreGrinder] undoes it. */
    fun deleteGrinder(grinder: Grinder) {
        viewModelScope.launch { grinderRepository.deleteGrinder(grinder.id) }
    }

    /** Puts back a grinder removed with [deleteGrinder] (the "Undo" action). */
    fun restoreGrinder(grinder: Grinder) {
        viewModelScope.launch { grinderRepository.addGrinder(grinder) }
    }

    /** Adds a coffee. Roaster and beans must not be blank; origin is optional. */
    fun addCoffeeBag(roasterName: String, beanName: String, origin: String, roastDate: LocalDate) {
        if (roasterName.isBlank() || beanName.isBlank()) return
        viewModelScope.launch {
            coffeeBagRepository.addCoffeeBag(
                CoffeeBag(
                    id = UUID.randomUUID().toString(),
                    roasterName = roasterName.trim(),
                    beanName = beanName.trim(),
                    origin = origin.trim(),
                    roastDate = roastDate,
                )
            )
        }
    }

    /** Deletes [coffeeBag]. Brew logs keep their copy; [restoreCoffeeBag] undoes it. */
    fun deleteCoffeeBag(coffeeBag: CoffeeBag) {
        viewModelScope.launch { coffeeBagRepository.deleteCoffeeBag(coffeeBag.id) }
    }

    /** Puts back a coffee removed with [deleteCoffeeBag] (the "Undo" action). */
    fun restoreCoffeeBag(coffeeBag: CoffeeBag) {
        viewModelScope.launch { coffeeBagRepository.addCoffeeBag(coffeeBag) }
    }

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
