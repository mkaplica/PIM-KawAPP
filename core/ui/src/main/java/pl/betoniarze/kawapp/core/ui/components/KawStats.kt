package pl.betoniarze.kawapp.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

/** One statistic, e.g. value "15 g" with label "coffee". */
@Immutable
data class KawStat(val value: String, val label: String)

/** A row of [stats] inside a card (recipe list, feed). Each stat takes an equal share of the width. */
@Composable
fun KawStatRow(
    stats: List<KawStat>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        stats.forEach { stat ->
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stat.value,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Text(
                    text = stat.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }
    }
}

/** [stats] as separate rounded tiles (recipe detail). Each tile takes an equal share of the width. */
@Composable
fun KawStatTiles(
    stats: List<KawStat>,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        stats.forEach { stat ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainer)
                    .padding(horizontal = 6.dp, vertical = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stat.value,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                )
                Text(
                    text = stat.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                )
            }
        }
    }
}

/** Formatting helpers for brew numbers shown in stats. */
object KawFormat {
    /** 250 ml / 15 g → "1:16,7" (decimal separator follows the Locale). Null when [coffeeGrams] is 0. */
    fun ratio(coffeeGrams: Int, waterMl: Int): String? {
        if (coffeeGrams <= 0) return null
        return String.format(Locale.getDefault(), "1:%.1f", waterMl.toFloat() / coffeeGrams)
    }

    /** Formats seconds as m:ss, e.g. 185 → "3:05". */
    fun duration(totalSeconds: Int): String =
        String.format(Locale.getDefault(), "%d:%02d", totalSeconds / 60, totalSeconds % 60)
}
