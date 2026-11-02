package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import pl.betoniarze.kawapp.core.data.fake.FakeCoffeeData
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

class FakeRecipeRepository : RecipeRepository {
    private val recipesFlow = MutableStateFlow(FakeCoffeeData.coffeeRecipes)

    override fun getRecipes(): Flow<List<CoffeeRecipe>> = recipesFlow.asStateFlow()

    override fun getRecipeById(id: String): Flow<CoffeeRecipe?> =
        recipesFlow.map { list -> list.find { it.id == id } }

    override suspend fun addRecipe(recipe: CoffeeRecipe) {
        recipesFlow.update { currentList -> currentList + recipe }
    }
}
