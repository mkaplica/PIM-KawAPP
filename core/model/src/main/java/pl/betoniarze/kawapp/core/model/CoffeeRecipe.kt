package pl.betoniarze.kawapp.core.model

data class  CoffeeRecipe(
    val id: String,
    val title: String,
    val brewMethod: BrewMethod,
    val coffeeGrams: Int,
    val totalWaterMl: Int,
    val notes: String,
    val steps: List<BrewStep>
)
