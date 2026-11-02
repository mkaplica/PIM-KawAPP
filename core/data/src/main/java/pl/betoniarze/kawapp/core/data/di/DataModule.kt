package pl.betoniarze.kawapp.core.data.di

import pl.betoniarze.kawapp.core.data.repository.FakeFeedRepository
import pl.betoniarze.kawapp.core.data.repository.FakeRecipeRepository
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository

object DataModule {
    fun provideFeedRepository(): FeedRepository = FakeFeedRepository()
    fun provideRecipeRepository(): RecipeRepository = FakeRecipeRepository()
}
