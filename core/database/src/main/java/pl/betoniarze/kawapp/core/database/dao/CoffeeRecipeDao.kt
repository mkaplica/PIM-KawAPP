package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.database.model.BrewStepEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeWithSteps

@Dao
interface CoffeeRecipeDao {
    @Transaction
    @Query("SELECT * FROM coffee_recipes")
    fun getCoffeeRecipes(): Flow<List<CoffeeRecipeWithSteps>>

    @Transaction
    @Query("SELECT * FROM coffee_recipes WHERE id = :id")
    fun getCoffeeRecipeById(id: String): Flow<CoffeeRecipeWithSteps?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoffeeRecipe(recipe: CoffeeRecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrewSteps(steps: List<BrewStepEntity>)

    @Transaction
    suspend fun insertCoffeeRecipeWithSteps(recipe: CoffeeRecipeEntity, steps: List<BrewStepEntity>) {
        insertCoffeeRecipe(recipe)
        insertBrewSteps(steps)
    }

    @Query("DELETE FROM coffee_recipes WHERE id = :id")
    suspend fun deleteCoffeeRecipe(id: String)

    @Query("DELETE FROM brew_steps WHERE recipeId = :recipeId")
    suspend fun deleteStepsByRecipeId(recipeId: String)
}
