package pl.betoniarze.kawapp.feature.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.feature.recipes.components.RecipeCard

@Composable
fun RecipesScreen(
    modifier: Modifier = Modifier,
    onRecipeClick: (String) -> Unit = {},
    viewModel: RecipesViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    RecipesScreenContent(
        uiState = uiState,
        modifier = modifier,
        onRecipeClick = onRecipeClick,
        onSearchQueryChange = viewModel::onSearchQueryChange,
        onBrewMethodChange = viewModel::onBrewMethodChange
    )
}

@Composable
fun RecipesScreenContent(
    modifier: Modifier = Modifier,
    uiState: RecipesUiState,
    onSearchQueryChange: (String) -> Unit = {},
    onBrewMethodChange: (BrewMethod?) -> Unit = {},
    onRecipeClick: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        when (uiState) {
            is RecipesUiState.Loading -> CircularProgressIndicator()
            is RecipesUiState.Success -> {
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
                    leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (uiState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = {onSearchQueryChange("")}) {
                                Icon(imageVector = Icons.Default.Clear, contentDescription = stringResource(R.string.clear_search_query))
                            }
                        }
                    },
                    singleLine = true
                )

                Spacer(modifier = Modifier.padding(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = uiState.selectedBrewMethod == null,
                            onClick = { onBrewMethodChange(null) },
                            label = { Text(text = stringResource(R.string.all_brewing_methods)) }
                        )
                    }
                    items(BrewMethod.entries.toTypedArray()) { method ->
                        FilterChip(
                            selected = uiState.selectedBrewMethod == method,
                            onClick = { onBrewMethodChange(method) },
                            label = { Text(text = method.displayName) }
                        )
                    }
                }

                Spacer(modifier = Modifier.padding(8.dp))

                if(uiState.recipes.isEmpty()) {
                    Text(text = stringResource(R.string.no_recipes_found))
                }
                else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.recipes,
                            key = { recipe -> recipe.id }
                        ) { recipe ->
                            RecipeCard(
                                recipe = recipe,
                                onClick = { onRecipeClick(recipe.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
