package pl.betoniarze.kawapp.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CoffeeMaker
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CoffeeMaker
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector
import pl.betoniarze.kawapp.core.ui.R

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @get:StringRes val titleTextId: Int,
    val route: String
) {
    FEED(
        selectedIcon = Icons.Filled.Groups,
        unselectedIcon = Icons.Outlined.Groups,
        titleTextId = R.string.feature_feed_title,
        route = "feed_route"
    ),
    RECIPES(
        selectedIcon = Icons.Filled.CoffeeMaker,
        unselectedIcon = Icons.Outlined.CoffeeMaker,
        titleTextId = R.string.feature_recipe_title,
        route = "recipes_route"
    ),
    PROFILE(
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person,
        titleTextId = R.string.feature_profile_title,
        route = "profile_route"
    )
}
