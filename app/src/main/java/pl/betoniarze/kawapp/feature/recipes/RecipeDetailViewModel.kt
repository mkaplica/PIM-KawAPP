package pl.betoniarze.kawapp.feature.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

/** State of the recipe detail screen. */
sealed interface RecipeDetailUiState {
    /** The recipe is not loaded yet; the screen shows the loading overlay. */
    data object Loading : RecipeDetailUiState
    /** The recipe was found. */
    data class Success(val recipe: CoffeeRecipe) : RecipeDetailUiState
    /** There is no recipe with the requested id (e.g. it was deleted). */
    data object NotFound : RecipeDetailUiState
}

/**
 * ViewModel of the recipe detail screen. Loads the recipe from the recipeId navigation argument.
 */
@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val recipeId: String = savedStateHandle.get<String>("recipeId") ?: ""

    val uiState: StateFlow<RecipeDetailUiState> = recipeRepository.getRecipeById(recipeId)
        .map { recipe ->
            if (recipe != null) {
                RecipeDetailUiState.Success(recipe)
            } else {
                RecipeDetailUiState.NotFound
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = RecipeDetailUiState.Loading
        )

    /** Deletes the recipe and then calls [onDeleted] (e.g. to navigate back). */
    fun deleteRecipe(onDeleted: () -> Unit) {
        viewModelScope.launch {
            recipeRepository.deleteRecipe(recipeId)
            onDeleted()
        }
    }
}
