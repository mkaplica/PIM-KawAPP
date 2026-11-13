package pl.betoniarze.kawapp.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.betoniarze.kawapp.core.data.repository.BrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.data.repository.LocalBrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.LocalFeedRepository
import pl.betoniarze.kawapp.core.data.repository.LocalRecipeRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        localRecipeRepository: LocalRecipeRepository
    ): RecipeRepository

    @Binds
    @Singleton
    abstract fun bindFeedRepository(
        localFeedRepository: LocalFeedRepository
    ): FeedRepository

    @Binds
    @Singleton
    abstract fun bindBrewLogRepository(
        localBrewLogRepository: LocalBrewLogRepository
    ): BrewLogRepository
}
