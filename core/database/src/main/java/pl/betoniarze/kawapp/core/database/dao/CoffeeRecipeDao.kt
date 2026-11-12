package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
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

    @Query("DELETE FROM coffee_recipes WHERE id = :id")
    suspend fun deleteCoffeeRecipe(id: String)
}
