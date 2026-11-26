package pl.betoniarze.kawapp.feature.journal.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.model.BrewLog
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawCard
import pl.betoniarze.kawapp.core.ui.components.KawTag
import pl.betoniarze.kawapp.core.ui.components.KawTagDefaults
import pl.betoniarze.kawapp.ui.components.RatingLabel

/**
 * Summary of one brew in the Journal list: method, time, beans, recipe, grinder and roast date.
 * The whole card is one click target. The method chip is neutral, because on this screen only the
 * streak uses the honey accent.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun BrewLogSummaryCard(
    brewLog: BrewLog,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val neutral = KawTagDefaults.neutral()

    KawCard(onClick = onClick, modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            KawTag(text = brewLog.brewMethod.displayName, colors = neutral)
            if (brewLog.rating > 0){
                RatingLabel(rating = brewLog.rating, modifier = Modifier.padding(start = 10.dp))
            }
            Text(
                text = brewLog.time,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.End,
            )
        }

        Text(
            text = "${brewLog.coffeeBag.origin} ${brewLog.coffeeBag.beanName}",
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = stringResource(
                R.string.brew_log_subtitle,
                brewLog.coffeeBag.roasterName,
                brewLog.recipeTitle,
            ),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            brewLog.grinder?.let { grinder ->
                KawTag(text = "${grinder.displayName} · ${brewLog.grindSetting}", colors = neutral)
            }
            KawTag(text = stringResource(R.string.roast_date, brewLog.coffeeBag.roastDate.toString()), colors = neutral)
        }
    }
}
