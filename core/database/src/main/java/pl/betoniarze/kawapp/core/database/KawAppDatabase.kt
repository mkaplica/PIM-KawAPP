package pl.betoniarze.kawapp.core.database

import androidx.room.Database
import pl.betoniarze.kawapp.core.database.model.BrewStepEntity
import pl.betoniarze.kawapp.core.database.model.CoffeeRecipeEntity
import pl.betoniarze.kawapp.core.database.model.FeedPostEntity
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.betoniarze.kawapp.core.database.dao.BrewLogDao
import pl.betoniarze.kawapp.core.database.dao.CoffeeRecipeDao
import pl.betoniarze.kawapp.core.database.dao.FeedPostDao
import pl.betoniarze.kawapp.core.database.model.BrewLogEntity
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

/** Room database of the app: user recipes with their steps, brew logs and feed posts. */
@TypeConverters(Converters::class)
abstract class KawAppDatabase : RoomDatabase() {
    /** DAO for community feed posts. */
    abstract fun feedPostDao(): FeedPostDao
    /** DAO for the user's recipes and their brew steps. */
    abstract fun coffeeRecipeDao(): CoffeeRecipeDao
    /** DAO for Journal brew logs. */
    abstract fun brewLogDao(): BrewLogDao
}
