package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

/** Source of brewing recipes: the user's own recipes and the built-in ones. */
interface RecipeRepository {
    /** All recipes: the user's own first, then the built-in ones. */
    fun getRecipes(): Flow<List<CoffeeRecipe>>
    /** The recipe with [id] (user or built-in), or null if there is none. */
    fun getRecipeById(id: String): Flow<CoffeeRecipe?>
    /** Saves a new user [recipe] together with its steps. */
    suspend fun addRecipe(recipe: CoffeeRecipe)
    /**
     * Overwrites an existing user [recipe] and replaces all of its steps. Built-in recipes cannot
     * be updated.
     */
    suspend fun updateRecipe(recipe: CoffeeRecipe)
    /** Deletes the user recipe with [id] and its steps. Built-in recipes cannot be deleted. */
    suspend fun deleteRecipe(id: String)
}
