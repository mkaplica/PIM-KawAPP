package pl.betoniarze.kawapp.core.model

data class CoffeeRecipe(
    val id: String,
    val title: String,
    val brewMethod: BrewMethod,
    val coffeeGrams: Int,
    val totalWaterMl: Int,
    val notes: String,
    val steps: List<BrewStep>,
    // Built-in recipe shipped with the app (not in the database): read-only.
    val isPredefined: Boolean = false
)
