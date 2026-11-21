package pl.betoniarze.kawapp.core.model

/**
 * A brewing recipe with its dose, water and steps.
 *
 * @property notes Free-text description shown on the recipe detail screen.
 * @property isPredefined True for built-in recipes shipped with the app. They are not stored in the
 * database and are read-only.
 */
data class CoffeeRecipe(
    val id: String,
    val title: String,
    val brewMethod: BrewMethod,
    val coffeeGrams: Int,
    val totalWaterMl: Int,
    val notes: String,
    val steps: List<BrewStep>,
    val isPredefined: Boolean = false
)
