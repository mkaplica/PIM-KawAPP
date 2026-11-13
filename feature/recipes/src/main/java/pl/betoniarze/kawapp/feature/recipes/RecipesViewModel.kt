package pl.betoniarze.kawapp.feature.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import kotlin.time.Duration.Companion.milliseconds

sealed interface RecipesUiState {
    data object Loading : RecipesUiState
    data class Success(
        val recipes: List<CoffeeRecipe>,
        val searchQuery: String = "",
        val selectedBrewMethod: BrewMethod? = null
    ) : RecipesUiState
}

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedBrewMethod = MutableStateFlow<BrewMethod?>(null)
    @OptIn(FlowPreview::class)
    private val debouncedQuery = _searchQuery
        .debounce(300.milliseconds)
        .distinctUntilChanged()

    val uiState: StateFlow<RecipesUiState> = combine(
        recipeRepository.getRecipes(),
        _searchQuery,
        debouncedQuery,
        _selectedBrewMethod
    ) { recipes, immediateQuery, queryToFilter, selectedMethod ->
        val filteredRecipes = recipes.filter { recipe ->
            val matchesQuery = recipe.title.contains(queryToFilter, ignoreCase = true) ||
                    recipe.notes.contains(queryToFilter, ignoreCase = true)
            val matchesBrewMethod =
                selectedMethod == null || recipe.brewMethod == selectedMethod
            matchesQuery && matchesBrewMethod
        }
        RecipesUiState.Success(
            recipes = filteredRecipes,
            searchQuery = immediateQuery,
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
