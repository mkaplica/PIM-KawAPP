package pl.betoniarze.kawapp.core.model

import java.time.LocalDate

/**
 * A single brew saved in the Journal: what was brewed, when, with which beans, grinder and recipe.
 *
 * @property time Time of day as text, e.g. "08:30".
 * @property recipeTitle Recipe title copied at brew time, so the log still reads well if the recipe
 * changes.
 * @property grindSetting Grinder setting as text, e.g. "22 clicks".
 */
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
