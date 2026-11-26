package pl.betoniarze.kawapp.core.model

/**
 * A brewing method. Built-in ones ([BUILT_IN]) are always available; the user can add more in
 * the Profile. Recipes and brew logs keep their own copy of [id] and [displayName].
 */

data class BrewMethod(
    val id: String,
    val displayName: String,
) {
    val isBuiltIn: Boolean get() = BUILT_IN.any { it.id == id }
    companion object {
        /** Built-in methods, also used by PredefinedRecipes and FakeCoffeeData. */
        val V60 = BrewMethod(id = "HARIO_V60", displayName = "Hario V60")
        val AEROPRESS = BrewMethod(id = "AEROPRESS", displayName = "AeroPress")
        /** Always available, also used by PredefinedRecipes; cannot be deleted. */
        val BUILT_IN = listOf(V60, AEROPRESS)
    }
}
