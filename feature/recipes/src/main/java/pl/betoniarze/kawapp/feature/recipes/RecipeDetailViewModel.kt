package pl.betoniarze.kawapp.feature.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.betoniarze.kawapp.core.data.repository.FakeRecipeRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

sealed interface RecipeDetailUiState {
    data object Loading : RecipeDetailUiState
    data class Success(val recipe: CoffeeRecipe) : RecipeDetailUiState
    data object NotFound : RecipeDetailUiState
}

class RecipeDetailViewModel (
    private val recipeId: String,
    private val recipeRepository: RecipeRepository = FakeRecipeRepository()
) : ViewModel() {

    val uiState: StateFlow<RecipeDetailUiState> = recipeRepository.getRecipeById(recipeId)
        .map { recipe ->
            if (recipe != null) {
                RecipeDetailUiState.Success(recipe)
            } else {
                RecipeDetailUiState.NotFound}
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecipeDetailUiState.Loading
        )
}
