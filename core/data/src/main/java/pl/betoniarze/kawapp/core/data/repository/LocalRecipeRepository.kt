package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.database.dao.CoffeeRecipeDao
import pl.betoniarze.kawapp.core.database.model.BrewStepEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeEntity
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

class LocalRecipeRepository (
    private val coffeeRecipeDao: CoffeeRecipeDao
) : RecipeRepository {
    override fun getRecipes(): Flow<List<CoffeeRecipe>> =
        coffeeRecipeDao.getCoffeeRecipes()
            .map { list -> list.map { it.asExternalModel() } }

    override fun getRecipeById(id: String): Flow<CoffeeRecipe?> =
        coffeeRecipeDao.getCoffeeRecipeById(id)
            .map { it?.asExternalModel() }

    override suspend fun addRecipe(recipe: CoffeeRecipe) {
        val recipeEntity = CoffeeRecipeEntity(
            id = recipe.id,
            title = recipe.title,
            brewMethod = recipe.brewMethod,
            coffeeGrams = recipe.coffeeGrams,
            totalWaterMl = recipe.totalWaterMl,
            notes = recipe.notes
        )

        val stepEntity = recipe.steps.map { step ->
            BrewStepEntity(
                recipeId = recipe.id,
                stepNumber = step.stepNumber,
                description = step.description,
                waterAmountMl = step.waterAmountMl,
                timeRange = step.timeRange
            )
        }

        coffeeRecipeDao.insertCoffeeRecipeWithSteps(recipeEntity, stepEntity)
    }
}