package pl.betoniarze.kawapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.betoniarze.kawapp.feature.home.HomeScreen
import pl.betoniarze.kawapp.feature.profile.ProfileScreen
import pl.betoniarze.kawapp.feature.recipes.RecipeDetailScreen
import pl.betoniarze.kawapp.feature.recipes.RecipesScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = TopLevelDestination.HOME.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = TopLevelDestination.HOME.route) {
            HomeScreen()
        }
        composable(route = TopLevelDestination.RECIPES.route) {
            RecipesScreen(
                onRecipeClick = { recipeId ->
                    navController.navigate("recipe_detail/$recipeId")
                }
            )
        }
        composable(route = TopLevelDestination.PROFILE.route) {
            ProfileScreen()
        }

        composable(route = "recipe_detail/{recipeId}") { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            RecipeDetailScreen(
                recipeId = recipeId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}
