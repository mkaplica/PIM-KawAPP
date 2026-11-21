package pl.betoniarze.kawapp.core.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.ui.R

/**
 * Honey tag with a flame icon showing the coffee streak, e.g. "3 day coffee streak" (Journal,
 * Feed).
 */
@Composable
fun KawStreakTag(
    streakDays: Int,
    modifier: Modifier = Modifier,
) {
    KawTag(
        text = pluralStringResource(R.plurals.streak_days, streakDays, streakDays),
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
        leading = {
            Icon(
                Icons.Filled.LocalFireDepartment,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        },
    )
}
