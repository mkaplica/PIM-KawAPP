package pl.betoniarze.kawapp.feature.recipes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.theme.navbarColor

@Composable
fun RecipeDetailScreen(
    modifier: Modifier = Modifier,
    recipeId: String = "",
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    RecipeDetailScreenContent(
        uiState = uiState,
        modifier = modifier,
        onBackClick = onBackClick
    )
}

@Composable
fun RecipeDetailScreenContent(
    modifier: Modifier = Modifier,
    uiState: RecipeDetailUiState,
    onBackClick: () -> Unit
) {
    val recipeNotFoundText = stringResource(R.string.recipe_not_found)
    val backText = stringResource(R.string.back)
    val descriptionLabel = stringResource(R.string.recipe_description)
    val methodLabel = stringResource(R.string.brew_method)
    val proportionsLabel = stringResource(R.string.proportions)
    val stepsLabel = stringResource(R.string.recipe_steps)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.navbarColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is RecipeDetailUiState.Loading -> CircularProgressIndicator()
            is RecipeDetailUiState.NotFound -> Text(
                text = recipeNotFoundText,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error
            )
            is RecipeDetailUiState.Success -> {
                val recipe = uiState.recipe
                val proportionsText = stringResource(
                    R.string.recipe_proportions_format,
                    recipe.coffeeGrams,
                    recipe.totalWaterMl
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = backText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.padding(16.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = recipe.title,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(descriptionLabel)
                                    append("\n")
                                }
                                withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                    append(recipe.notes)
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium
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
                                    append(proportionsLabel)
                                    append(" ")
                                }
                                append(proportionsText)
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append(stepsLabel)
                                    append("\n")
                                }
                                recipe.steps.forEachIndexed { index, step ->
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append("${index + 1}")
                                    }
                                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                        append(". ${step.description}")
                                    }
                                    append("\n")
                                }
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}