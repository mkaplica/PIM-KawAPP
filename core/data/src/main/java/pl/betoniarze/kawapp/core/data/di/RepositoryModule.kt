package pl.betoniarze.kawapp.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.betoniarze.kawapp.core.data.repository.BrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.BrewMethodRepository
import pl.betoniarze.kawapp.core.data.repository.CoffeeBagRepository
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.data.repository.GrinderRepository
import pl.betoniarze.kawapp.core.data.repository.LocalBrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.LocalBrewMethodRepository
import pl.betoniarze.kawapp.core.data.repository.LocalCoffeeBagRepository
import pl.betoniarze.kawapp.core.data.repository.LocalFeedRepository
import pl.betoniarze.kawapp.core.data.repository.LocalGrinderRepository
import pl.betoniarze.kawapp.core.data.repository.LocalRecipeRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import javax.inject.Singleton

/** Hilt module binding repository interfaces to their local (Room-based) implementations. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    /**
     * Recipes come from Room plus the built-in
     * [pl.betoniarze.kawapp.core.data.fake.PredefinedRecipes].
     */
    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        localRecipeRepository: LocalRecipeRepository
    ): RecipeRepository

    /** Feed posts come from Room (plus sample posts in debug builds). */
    @Binds
    @Singleton
    abstract fun bindFeedRepository(
        localFeedRepository: LocalFeedRepository
    ): FeedRepository

    /** Brew logs come from Room (plus sample logs in debug builds). */
    @Binds
    @Singleton
    abstract fun bindBrewLogRepository(
        localBrewLogRepository: LocalBrewLogRepository
    ): BrewLogRepository

    @Binds
    @Singleton
    abstract fun bindGrinderRepository(local: LocalGrinderRepository): GrinderRepository

    @Binds
    @Singleton
    abstract fun bindCoffeeBagRepository(local: LocalCoffeeBagRepository): CoffeeBagRepository

    @Binds
    @Singleton
    abstract fun bindBrewMethodRepository(local: LocalBrewMethodRepository): BrewMethodRepository
}
