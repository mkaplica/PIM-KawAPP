package pl.betoniarze.kawapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import pl.betoniarze.kawapp.navigation.TopLevelDestination

/** Creates and remembers the [AppState] for the current composition. */
@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): AppState {
    return remember(navController, coroutineScope) {
        AppState(navController, coroutineScope)
    }
}

/** UI state of the app shell: navigation and bottom bar logic shared by [AppScaffold]. */
@Stable
class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope
) {
    /**
     * Destination currently on top of the back stack. Each read subscribes separately and starts at
     * null, so read it once per composition.
     */
    val currentDestination: NavDestination?
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination

    /**
     * Null means the NavHost hasn't reported its first destination yet; the start
     * destination is a top-level one, so keep the bar instead of popping it in later.
     */
    fun shouldShowBottomBar(destination: NavDestination?): Boolean =
        destination == null || topLevelDestinations.any { destination.isRouteInHierarchy(it.route) }

    /** Tabs of the bottom navigation bar, in display order. */
    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * Switches to [topLevelDestination]'s tab. Pops back to the start screen, saves the state of
     * the tab being left and restores the state of the target tab, so each tab keeps its own back
     * stack and scroll position.
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.FEED -> navController.navigate(topLevelDestination.route, topLevelNavOptions)
            TopLevelDestination.RECIPES -> navController.navigate(topLevelDestination.route, topLevelNavOptions)
            TopLevelDestination.JOURNAL -> navController.navigate(topLevelDestination.route, topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigate(topLevelDestination.route, topLevelNavOptions)
        }
    }
}

/** True if this destination or one of its parent graphs has [route]. */
fun NavDestination?.isRouteInHierarchy(route: String): Boolean =
    this?.hierarchy?.any { it.route == route } == true
