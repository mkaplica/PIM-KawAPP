package pl.betoniarze.kawapp.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.betoniarze.kawapp.feature.feed.FeedScreen
import pl.betoniarze.kawapp.feature.journal.JournalScreen
import pl.betoniarze.kawapp.feature.profile.ProfileScreen
import pl.betoniarze.kawapp.feature.recipes.RecipeAddScreen
import pl.betoniarze.kawapp.feature.recipes.RecipeDetailScreen
import pl.betoniarze.kawapp.feature.recipes.RecipesScreen

/** Crossfade duration between screens (the NavHost default is 700 ms). */
private const val SCREEN_FADE_MS = 1_000

/**
 * Navigation graph of the app: the four top-level tabs plus recipe detail, add and edit screens.
 *
 * All screen changes use a [SCREEN_FADE_MS] crossfade.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = TopLevelDestination.FEED.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        // Same crossfade as the NavHost default (700 ms), just slower.
        // Loading flicker is covered by KawLoadingOverlay.
        enterTransition = { fadeIn(tween(SCREEN_FADE_MS)) },
        exitTransition = { fadeOut(tween(SCREEN_FADE_MS)) },
        popEnterTransition = { fadeIn(tween(SCREEN_FADE_MS)) },
        popExitTransition = { fadeOut(tween(SCREEN_FADE_MS)) },
    ) {
        composable(route = TopLevelDestination.FEED.route) {
            FeedScreen()
        }
        composable(route = TopLevelDestination.RECIPES.route) {
            RecipesScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate("recipe_detail/$recipeId")
                },
                onAddRecipeClick = { navController.navigate("add_recipe") }
            )
        }

        composable(route = TopLevelDestination.JOURNAL.route) {
            JournalScreen()
        }
        composable(route = TopLevelDestination.PROFILE.route) {
            ProfileScreen()
        }

        composable(route = "recipe_detail/{recipeId}") {
            RecipeDetailScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onEditClick = { recipeId ->
                    navController.navigate("edit_recipe/$recipeId")
                },
                onRecipeDeleted = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = "add_recipe") {
            RecipeAddScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        // Same form as add_recipe; the ViewModel pre-fills it from the recipeId argument.
        composable(route = "edit_recipe/{recipeId}") {
            RecipeAddScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
