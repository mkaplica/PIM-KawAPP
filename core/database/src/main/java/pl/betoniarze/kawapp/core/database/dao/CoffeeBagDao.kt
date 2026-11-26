package pl.betoniarze.kawapp.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.database.model.SavedCoffeeBagEntity

/** Access to the coffee_bags table (coffees kept in the Profile). */
@Dao
interface CoffeeBagDao {
    /** All saved coffees, newest first. Emits again whenever the table changes. */
    @Query("SELECT * FROM coffee_bags ORDER BY createdAt DESC")
    fun getCoffeeBags(): Flow<List<SavedCoffeeBagEntity>>

    /** Inserts [coffeeBag], replacing one with the same id. */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCoffeeBag(coffeeBag: SavedCoffeeBagEntity)

    /** Deletes the coffee with [id]. Does nothing if it does not exist. */
    @Query("DELETE FROM coffee_bags WHERE id = :id")
    suspend fun deleteCoffeeBag(id: String)
}
