package pl.betoniarze.kawapp.feature.recipes.brew.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerState

@Composable
fun BrewStartFab(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        text = {
            Text(stringResource(R.string.start_timer))
        },
        icon = {
            Icon(
                imageVector = Icons.Filled.LocalCafe,
                contentDescription = null
            )
        },
    )
}

@Composable
fun BrewTimerBottomBar(
    state: BrewTimerState,
    onToggle: () -> Unit,
    onReset: () -> Unit,
    onSkip: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BottomAppBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        TimerControls(
            state = state,
            onToggle = onToggle,
            onReset = onReset,
            onSkip = onSkip,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
    }
}