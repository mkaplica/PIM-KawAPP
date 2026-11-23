package pl.betoniarze.kawapp.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pl.betoniarze.kawapp.core.model.AppLanguage
import pl.betoniarze.kawapp.core.model.ThemeMode
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawLoadingOverlay
import pl.betoniarze.kawapp.feature.chime_generator.ChimeTone

/** Profile tab connected to [ProfileViewModel]: app language, theme and brew chime settings. */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val selectedLanguage by viewModel.selectedLanguage.collectAsState()
    val selectedThemeMode by viewModel.selectedThemeMode.collectAsState()
    val selectedChimeTone by viewModel.selectedChimeTone.collectAsState()
    val isApplyingSettings by viewModel.isApplyingSettings.collectAsState()

    ProfileScreenContent(
        selectedLanguage = selectedLanguage,
        selectedThemeMode = selectedThemeMode,
        isApplyingSettings = isApplyingSettings,
        onLanguageSelected = viewModel::selectLanguage,
        onThemeModeSelected = viewModel::selectThemeMode,
        selectedChimeTone = selectedChimeTone,
        onChimeToneSelected = viewModel::selectChimeTone,
        modifier = modifier
    )
}

/**
 * Stateless Profile UI: language, theme and brew chime setting cards, plus the overlay shown while a change is
 * applied.
 *
 * @param isApplyingSettings True while a language/theme change is being applied; shows the loading
 * overlay.
 */
@Composable
fun ProfileScreenContent(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
    selectedThemeMode: ThemeMode = ThemeMode.SYSTEM,
    isApplyingSettings: Boolean = false,
    onThemeModeSelected: (ThemeMode) -> Unit = {},
    selectedChimeTone: ChimeTone = ChimeTone.FIRST,
    onChimeToneSelected: (ChimeTone) -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingCard(
                title = stringResource(R.string.profile_language_setting),
                options = AppLanguage.entries,
                selected = selectedLanguage,
                onSelected = onLanguageSelected,
                label = { language ->
                    when (language) {
                        AppLanguage.POLISH -> stringResource(R.string.language_polish)
                        AppLanguage.ENGLISH -> stringResource(R.string.language_english)
                    }
                },
            )

            SettingCard(
                title = stringResource(R.string.profile_theme_setting),
                options = ThemeMode.entries,
                selected = selectedThemeMode,
                onSelected = onThemeModeSelected,
                label = { mode ->
                    when (mode) {
                        ThemeMode.SYSTEM -> stringResource(R.string.theme_system)
                        ThemeMode.LIGHT -> stringResource(R.string.theme_light)
                        ThemeMode.DARK -> stringResource(R.string.theme_dark)
                    }
                },
            )

            SettingCard(
                title = stringResource(R.string.profile_chime_setting),
                options = ChimeTone.entries,
                selected = selectedChimeTone,
                onSelected = onChimeToneSelected,
                label = { tone ->
                    when (tone) {
                        ChimeTone.FIRST -> stringResource(R.string.chime_high)
                        ChimeTone.SECOND -> stringResource(R.string.chime_low)
                        ChimeTone.BOTH -> stringResource(R.string.chime_both)
                    }
                },
            )
        }

        // Covers the Activity being recreated after a language/theme change.
        // Already visible when the screen comes back, so don't fade it in again.
        KawLoadingOverlay(visible = isApplyingSettings, animateFirstShow = false)
    }
}

/** A settings card: [title] and a segmented single choice between [options]. */
@Composable
private fun <T> SettingCard(
    title: String,
    options: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    label: @Composable (T) -> String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                options.forEachIndexed { index, option ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                        onClick = { if (option != selected) onSelected(option) },
                        selected = option == selected,
                    ) {
                        Text(label(option))
                    }
                }
            }
        }
    }
}
