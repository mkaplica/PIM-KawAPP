package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

interface RecipeRepository {
    fun getRecipes(): Flow<List<CoffeeRecipe>>
    fun getRecipeById(id: String): Flow<CoffeeRecipe?>
    suspend fun addRecipe(recipe: CoffeeRecipe)
    suspend fun updateRecipe(recipe: CoffeeRecipe)
    suspend fun deleteRecipe(id: String)
}
