package pl.betoniarze.kawapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.betoniarze.kawapp.feature.feed.FeedScreen
import pl.betoniarze.kawapp.feature.journal.JournalScreen
import pl.betoniarze.kawapp.feature.profile.ProfileScreen
import pl.betoniarze.kawapp.feature.recipes.RecipeDetailScreen
import pl.betoniarze.kawapp.feature.recipes.RecipesScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = TopLevelDestination.FEED.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = TopLevelDestination.FEED.route) {
            FeedScreen()
        }
        composable(route = TopLevelDestination.RECIPES.route) {
            RecipesScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate("recipe_detail/$recipeId")
                }
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
                }
            )
        }
    }
}
