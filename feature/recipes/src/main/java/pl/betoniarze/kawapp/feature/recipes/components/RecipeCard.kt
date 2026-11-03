package pl.betoniarze.kawapp.feature.recipes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.ui.R

@Composable
fun RecipeCard(
    recipe: CoffeeRecipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val ratioLabel = stringResource(R.string.ratio)
    val methodLabel = stringResource(R.string.brew_method)
    val proportionsLabel = stringResource(R.string.proportions)
    val proportionsText = stringResource(
        R.string.recipe_proportions_format,
        recipe.coffeeGrams,
        recipe.totalWaterMl
    )

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(methodLabel)
                        append(" ")
                    }
                    append(recipe.brewMethod.displayName)
                },
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(ratioLabel)
                        append(" ")
                    }
                    append("1:")
                    val ratio = recipe.totalWaterMl / recipe.coffeeGrams
                    append(ratio.toString())
                }
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(proportionsLabel)
                        append(" ")
                    }
                    append(proportionsText)
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
