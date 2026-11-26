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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import pl.betoniarze.kawapp.core.data.repository.BrewMethodRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import kotlin.time.Duration.Companion.milliseconds

/** State of the recipe list. */
sealed interface RecipesUiState {
    /** Recipes are not loaded yet; the screen shows the loading overlay. */
    data object Loading : RecipesUiState
    /**
     * Recipes are loaded.
     *
     * @property recipes Recipes matching the search query and the brew method filter.
     * @property searchQuery Text in the search field (updates immediately, filtering is debounced).
     * @property selectedBrewMethod Active filter, or null for all methods.
     * @property brewMethods Filter chips: built-in and Profile methods, plus any method still used
     * by a recipe after it was deleted from the Profile.
     */
    data class Success(
        val recipes: List<CoffeeRecipe>,
        val searchQuery: String = "",
        val selectedBrewMethod: BrewMethod? = null,
        val brewMethods: List<BrewMethod> = emptyList(),
    ) : RecipesUiState
}

/** ViewModel of the recipe list: search and brew method filtering. */
@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository,
    brewMethodRepository: BrewMethodRepository,
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedBrewMethod = MutableStateFlow<BrewMethod?>(null)
    @OptIn(FlowPreview::class)
    private val debouncedQuery = _searchQuery
        .debounce(300.milliseconds)
        .distinctUntilChanged()

    /** Filtered recipes. Search filtering waits 300 ms after the last keystroke. */
    val uiState: StateFlow<RecipesUiState> = combine(
        recipeRepository.getRecipes(),
        _searchQuery,
        debouncedQuery,
        _selectedBrewMethod,
        brewMethodRepository.getBrewMethods(),
    ) { recipes, immediateQuery, queryToFilter, selectedMethod, methods ->
        val filteredRecipes = recipes.filter { recipe ->
            val matchesQuery = recipe.title.contains(queryToFilter, ignoreCase = true) ||
                    recipe.notes.contains(queryToFilter, ignoreCase = true)
            val matchesBrewMethod =
                selectedMethod == null || recipe.brewMethod.id == selectedMethod.id
            matchesQuery && matchesBrewMethod
        }
        RecipesUiState.Success(
            recipes = filteredRecipes,
            searchQuery = immediateQuery,
            selectedBrewMethod = selectedMethod,
            brewMethods = (methods + recipes.map { it.brewMethod }).distinctBy { it.id },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = RecipesUiState.Loading
    )

    /** Updates the search text; the list is filtered by title and notes. */
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    /** Filters by [newMethod]; null shows all methods. */
    fun onBrewMethodChange(newMethod: BrewMethod?) {
        _selectedBrewMethod.value = newMethod
    }
}
