package pl.betoniarze.kawapp.core.data.di

import android.content.Context
import androidx.room.Room
import pl.betoniarze.kawapp.core.data.repository.BrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.data.repository.LocalBrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.LocalFeedRepository
import pl.betoniarze.kawapp.core.data.repository.LocalRecipeRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.database.KawAppDatabase


// WYWOŁANIE Z BAZĄ DANYCH
object DataModule {

    @Volatile
    private var INSTANCE: KawAppDatabase? = null

    fun getDatabase(context: Context): KawAppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                KawAppDatabase::class.java,
                "kawapp.db"
            ).build()
            INSTANCE = instance
            instance
        }
    }

    fun provideFeedRepository(context: Context): FeedRepository {
        return LocalFeedRepository(getDatabase(context).feedPostDao())
    }

    fun provideRecipeRepository(context: Context): RecipeRepository {
        return LocalRecipeRepository(getDatabase(context).coffeeRecipeDao())
    }

    fun provideBrewLogRepository(context: Context): BrewLogRepository {
        return LocalBrewLogRepository(getDatabase(context).brewLogDao())
    }
}


// WYWOŁANIE Z FEJKOWYM REPOZYTORIUM

//object DataModule {
//    fun provideFeedRepository(): FeedRepository = FakeFeedRepository()
//    fun provideRecipeRepository(): RecipeRepository = FakeRecipeRepository()
//}
