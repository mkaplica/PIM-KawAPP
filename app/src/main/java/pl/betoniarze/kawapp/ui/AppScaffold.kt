package pl.betoniarze.kawapp.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import pl.betoniarze.kawapp.navigation.AppNavHost

import androidx.compose.material3.MaterialTheme
import pl.betoniarze.kawapp.core.ui.theme.navbarColor

@Composable
fun AppScaffold(
    appState: AppState = rememberAppState()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (appState.shouldShowBottomBar) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.navbarColor
                ) {
                    appState.topLevelDestinations.forEach { destination ->
                        val selected = appState.currentDestination.isRouteInHierarchy(destination.route)
                        val icon = if (selected) destination.selectedIcon else destination.unselectedIcon
                        NavigationBarItem(
                            selected = selected,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            icon = { Icon(icon, contentDescription = null) },
                            label = { Text(stringResource(destination.titleTextId)) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        AppNavHost(
            navController = appState.navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
