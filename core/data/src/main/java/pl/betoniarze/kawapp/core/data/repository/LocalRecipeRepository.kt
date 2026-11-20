package pl.betoniarze.kawapp.core.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.database.dao.CoffeeRecipeDao
import pl.betoniarze.kawapp.core.database.model.BrewStepEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeEntity
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

class LocalRecipeRepository @Inject constructor(
    private val coffeeRecipeDao: CoffeeRecipeDao
) : RecipeRepository {
    override fun getRecipes(): Flow<List<CoffeeRecipe>> =
        coffeeRecipeDao.getCoffeeRecipes()
            .map { list -> list.map { it.asExternalModel() } }

    override fun getRecipeById(id: String): Flow<CoffeeRecipe?> =
        coffeeRecipeDao.getCoffeeRecipeById(id)
            .map { it?.asExternalModel() }

    override suspend fun addRecipe(recipe: CoffeeRecipe) {
        coffeeRecipeDao.insertCoffeeRecipeWithSteps(recipe.asEntity(), recipe.asStepEntities())
    }

    override suspend fun updateRecipe(recipe: CoffeeRecipe) {
        coffeeRecipeDao.updateCoffeeRecipeWithSteps(recipe.asEntity(), recipe.asStepEntities())
    }

    override suspend fun deleteRecipe(id: String) {
        // Brew steps are removed by the ON DELETE CASCADE foreign key.
        coffeeRecipeDao.deleteCoffeeRecipe(id)
    }
}

private fun CoffeeRecipe.asEntity() = CoffeeRecipeEntity(
    id = id,
    title = title,
    brewMethod = brewMethod,
    coffeeGrams = coffeeGrams,
    totalWaterMl = totalWaterMl,
    notes = notes
)

private fun CoffeeRecipe.asStepEntities() = steps.map { step ->
    BrewStepEntity(
        recipeId = id,
        stepNumber = step.stepNumber,
        description = step.description,
        waterAmountMl = step.waterAmountMl,
        timeRange = step.timeRange
    )
}
