package pl.betoniarze.kawapp.feature.recipes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawCard
import pl.betoniarze.kawapp.core.ui.components.KawFormat
import pl.betoniarze.kawapp.core.ui.components.KawStat
import pl.betoniarze.kawapp.core.ui.components.KawStatRow
import pl.betoniarze.kawapp.core.ui.components.KawTag
import pl.betoniarze.kawapp.feature.recipes.brew.toTimedSteps

/**
 * Karta przepisu. Na ekranie listy przepisów miodowy akcent ma TYLKO chip metody.
 */
@Composable
fun RecipeCard(
    recipe: CoffeeRecipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSeconds = remember(recipe) { recipe.toTimedSteps().maxOfOrNull { it.endSec } }
    val stats = buildList {
        add(KawStat("${recipe.coffeeGrams} g", stringResource(R.string.stat_coffee)))
        add(KawStat("${recipe.totalWaterMl} ml", stringResource(R.string.stat_water)))
        KawFormat.ratio(recipe.coffeeGrams, recipe.totalWaterMl)?.let {
            add(KawStat(it, stringResource(R.string.stat_ratio)))
        }
        totalSeconds?.let { add(KawStat(KawFormat.duration(it), stringResource(R.string.stat_time))) }
    }

    KawCard(onClick = onClick, modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            KawTag(text = recipe.brewMethod.displayName)
            Text(
                text = pluralStringResource(R.plurals.steps_count, recipe.steps.size, recipe.steps.size),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        KawStatRow(stats = stats)
    }
}
