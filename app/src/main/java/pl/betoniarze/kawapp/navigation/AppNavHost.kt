package pl.betoniarze.kawapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import pl.betoniarze.kawapp.feature.home.HomeScreen
import pl.betoniarze.kawapp.feature.profile.ProfileScreen
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
            RecipesScreen()
        }
        composable(route = TopLevelDestination.PROFILE.route) {
            ProfileScreen()
        }
    }
}
