package pl.betoniarze.kawapp.core.data.repository

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import pl.betoniarze.kawapp.core.data.fake.PredefinedRecipes
import pl.betoniarze.kawapp.core.database.dao.CoffeeRecipeDao
import pl.betoniarze.kawapp.core.database.model.BrewStepEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeEntity
import pl.betoniarze.kawapp.core.database.model.asExternalModel
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

class LocalRecipeRepository @Inject constructor(
    private val coffeeRecipeDao: CoffeeRecipeDao
) : RecipeRepository {
    // The user's own recipes first, then the built-in ones (always present).
    //
    // TODO: Problem to solve: should users be able to delete (or hide) predefined recipes?
    //  Right now they always show up and the detail screen hides edit/delete for them,
    //  because they live in PredefinedRecipes, not in the database. Possible approaches:
    //  - store hidden predefined ids (DataStore or a small Room table) and filter them out here,
    //  - or seed predefined recipes into the database on first launch, so they become
    //    regular recipes that can be edited and deleted.
    //  Brew logs reference predefined recipe ids, so decide what a log shows for a deleted one.
    override fun getRecipes(): Flow<List<CoffeeRecipe>> =
        coffeeRecipeDao.getCoffeeRecipes()
            .map { list -> list.map { it.asExternalModel() } + PredefinedRecipes.recipes }

    override fun getRecipeById(id: String): Flow<CoffeeRecipe?> =
        coffeeRecipeDao.getCoffeeRecipeById(id)
            .map { it?.asExternalModel() ?: PredefinedRecipes.findById(id) }

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
