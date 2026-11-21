package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.database.model.BrewStepEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeWithSteps

/** Access to the coffee_recipes table and its brew_steps. */
@Dao
interface CoffeeRecipeDao {
    /** All user recipes together with their steps. Emits again whenever either table changes. */
    @Transaction
    @Query("SELECT * FROM coffee_recipes")
    fun getCoffeeRecipes(): Flow<List<CoffeeRecipeWithSteps>>

    /** The recipe with [id] and its steps, or null if it does not exist. */
    @Transaction
    @Query("SELECT * FROM coffee_recipes WHERE id = :id")
    fun getCoffeeRecipeById(id: String): Flow<CoffeeRecipeWithSteps?>

    /** Inserts [recipe] without its steps, replacing a recipe with the same id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoffeeRecipe(recipe: CoffeeRecipeEntity)

    /** Inserts [steps], replacing steps with the same id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrewSteps(steps: List<BrewStepEntity>)

    /** Inserts [recipe] and its [steps] in one transaction. */
    @Transaction
    suspend fun insertCoffeeRecipeWithSteps(recipe: CoffeeRecipeEntity, steps: List<BrewStepEntity>) {
        insertCoffeeRecipe(recipe)
        insertBrewSteps(steps)
    }

    /** Updates the recipe row only; its steps are left as they are. */
    @Update
    suspend fun updateCoffeeRecipe(recipe: CoffeeRecipeEntity)

    /** Updates [recipe] and replaces all of its steps with [steps], in one transaction. */
    @Transaction
    suspend fun updateCoffeeRecipeWithSteps(recipe: CoffeeRecipeEntity, steps: List<BrewStepEntity>) {
        updateCoffeeRecipe(recipe)
        deleteStepsByRecipeId(recipe.id)
        insertBrewSteps(steps)
    }

    /** Deletes the recipe with [id]. Its steps are removed by the ON DELETE CASCADE foreign key. */
    @Query("DELETE FROM coffee_recipes WHERE id = :id")
    suspend fun deleteCoffeeRecipe(id: String)

    /** Deletes all steps of the recipe with [recipeId]. */
    @Query("DELETE FROM brew_steps WHERE recipeId = :recipeId")
    suspend fun deleteStepsByRecipeId(recipeId: String)
}
