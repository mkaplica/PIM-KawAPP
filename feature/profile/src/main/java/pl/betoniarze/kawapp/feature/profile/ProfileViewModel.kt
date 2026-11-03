package pl.betoniarze.kawapp.feature.profile

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.betoniarze.kawapp.core.model.AppLanguage

class ProfileViewModel : ViewModel() {

    private val _selectedLanguage = MutableStateFlow(getCurrentAppLanguage())
    val selectedLanguage: StateFlow<AppLanguage> = _selectedLanguage.asStateFlow()

    fun selectLanguage(language: AppLanguage) {
        if (_selectedLanguage.value == language) return
        _selectedLanguage.value = language
        val localeList = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

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
