package pl.betoniarze.kawapp.core.database

import androidx.room.Database
import pl.betoniarze.kawapp.core.database.model.BrewStepEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeEntity
import pl.betoniarze.kawapp.core.database.model.FeedPostEntity
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.betoniarze.kawapp.core.database.dao.BrewLogDao
import pl.betoniarze.kawapp.core.database.dao.BrewStepDao
import pl.betoniarze.kawapp.core.database.dao.CoffeeRecipeDao
import pl.betoniarze.kawapp.core.database.dao.FeedPostDao
import pl.betoniarze.kawapp.core.database.model.BrewLogEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeBagEntity
import pl.betoniarze.kawapp.core.database.utils.Converters


@Database(
    entities = [
        FeedPostEntity::class,
        CoffeeRecipeEntity::class,
        BrewStepEntity::class,
        BrewLogEntity::class
    ],
    version = 1,
    exportSchema = true
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedPostDao(): FeedPostDao
    abstract fun coffeeRecipeDao(): CoffeeRecipeDao
    abstract fun brewLogDao(): BrewLogDao
    abstract fun brewStepDao(): BrewStepDao
}
