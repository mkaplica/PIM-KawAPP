package pl.betoniarze.kawapp.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import pl.betoniarze.kawapp.core.ui.theme.navbarColor
import pl.betoniarze.kawapp.navigation.AppNavHost

@Composable
fun AppScaffold(
    appState: AppState = rememberAppState()
) {
    val colors = MaterialTheme.colorScheme
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        // Top inset is left to the screens, so headers (e.g. the Journal calendar)
        // can draw behind the status bar in their own color.
        contentWindowInsets = WindowInsets.systemBars.only(
            WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom
        ),
        bottomBar = {
            // Read the destination once: every currentBackStackEntryAsState() call
            // starts at null, so reading it per tab hid the bar for a frame (flicker).
            val currentDestination = appState.currentDestination
            if (appState.shouldShowBottomBar(currentDestination)) {
                NavigationBar(containerColor = colors.navbarColor) {
                    appState.topLevelDestinations.forEach { destination ->
                        val selected = currentDestination.isRouteInHierarchy(destination.route)
                        val icon = if (selected) destination.selectedIcon else destination.unselectedIcon
                        NavigationBarItem(
                            selected = selected,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = { Icon(icon, contentDescription = null) },
                            label = {
                                Text(
                                    text = stringResource(destination.titleTextId),
                                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = colors.onPrimary,
                                indicatorColor = colors.primary,
                                selectedTextColor = colors.onSurface,
                                unselectedIconColor = colors.onSurfaceVariant,
                                unselectedTextColor = colors.onSurfaceVariant,
                            ),
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Consume the insets applied here, so nested Scaffolds and TopAppBars
        // don't add the navigation bar padding a second time.
        AppNavHost(
            navController = appState.navController,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        )
    }
}
