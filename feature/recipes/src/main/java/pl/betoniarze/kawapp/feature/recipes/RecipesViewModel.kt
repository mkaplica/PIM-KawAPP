package pl.betoniarze.kawapp.feature.recipes

import android.R.attr.query
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.betoniarze.kawapp.core.data.repository.FakeRecipeRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

sealed interface RecipesUiState {
    data object Loading : RecipesUiState
    data class Success(
        val recipes: List<CoffeeRecipe>,
        val searchQuery: String = "",
        val selectedBrewMethod: BrewMethod? = null
    ) : RecipesUiState
}

class RecipesViewModel(
    private val recipeRepository: RecipeRepository = FakeRecipeRepository()
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedBrewMethod = MutableStateFlow<BrewMethod?>(null)

    val uiState: StateFlow<RecipesUiState> = combine(
        recipeRepository.getRecipes(),
        _searchQuery,
        _selectedBrewMethod
    ) { recipes, query, selectedMethod ->
        val filteredRecipes = recipes.filter { recipe ->
            val matchesQuery = recipe.title.contains(query, ignoreCase = true) ||
                    recipe.notes.contains(query, ignoreCase = true)
            val matchesBrewMethod =
                selectedMethod == null || recipe.brewMethod == selectedMethod
            matchesQuery && matchesBrewMethod
        }
        RecipesUiState.Success(
            recipes = filteredRecipes,
            searchQuery = query,
            selectedBrewMethod = selectedMethod
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecipesUiState.Loading
    )

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onBrewMethodChange(newMethod: BrewMethod?) {
        _selectedBrewMethod.value = newMethod
    }
}
