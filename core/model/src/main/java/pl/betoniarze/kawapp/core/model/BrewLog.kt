package pl.betoniarze.kawapp.core.model

import java.time.LocalDate

data class BrewLog(
    val id: String,
    val brewMethod: BrewMethod,
    val date: LocalDate,
    val time: String,
    val coffeeBag: CoffeeBag,
    val recipeId: String,
    val recipeTitle: String,
    val grinder: Grinder,
    val grindSetting: String,
    val tastingNotes: String
)
