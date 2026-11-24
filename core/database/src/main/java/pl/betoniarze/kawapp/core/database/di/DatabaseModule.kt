package pl.betoniarze.kawapp.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.betoniarze.kawapp.core.database.KawAppDatabase
import pl.betoniarze.kawapp.core.database.dao.BrewLogDao
import pl.betoniarze.kawapp.core.database.dao.CoffeeRecipeDao
import pl.betoniarze.kawapp.core.database.dao.FeedPostDao
import javax.inject.Singleton

/** Hilt module providing the Room database and its DAOs. */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /** Single app-wide instance of the database, stored in "kawapp.db". */
    @Provides
    @Singleton
    fun provideKawAppDatabase(
        @ApplicationContext context: Context
    ): KawAppDatabase {
        return Room.databaseBuilder(
            context,
            KawAppDatabase::class.java,
            "kawapp.db"
        ).build()
    }

    /** Recipe DAO from the shared database. */
    @Provides
    fun provideCoffeeRecipeDao(database: KawAppDatabase): CoffeeRecipeDao {
        return database.coffeeRecipeDao()
    }

    /** Feed post DAO from the shared database. */
    @Provides
    fun provideFeedPostDao(database: KawAppDatabase): FeedPostDao {
        return database.feedPostDao()
    }

    /** Brew log DAO from the shared database. */
    @Provides
    fun provideBrewLogDao(database: KawAppDatabase): BrewLogDao {
        return database.brewLogDao()
    }
}
