package pl.betoniarze.kawapp.feature.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
        onRecipeClick = onRecipeClick
    )
}

@Composable
fun RecipesScreenContent(
    modifier: Modifier = Modifier,
    uiState: RecipesUiState,
    onRecipeClick: (String) -> Unit = {}
) {
    val emptyLabel = stringResource(R.string.no_recipes_found)
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is RecipesUiState.Loading -> CircularProgressIndicator()
            is RecipesUiState.Success -> {
                if(uiState.recipes.isEmpty()) {
                    Text(text = emptyLabel)
                }
                else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 8.dp, start = 8.dp, end = 8.dp),
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
