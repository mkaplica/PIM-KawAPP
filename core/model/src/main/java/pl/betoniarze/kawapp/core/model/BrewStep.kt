package pl.betoniarze.kawapp.core.model

/**
 * One step of a recipe.
 *
 * @property stepNumber 1-based position in the recipe.
 * @property waterAmountMl Water poured in this step (not cumulative).
 * @property timeRange When the step happens, e.g. "0:00 - 0:45".
 */
data class BrewStep(
    val stepNumber: Int,
    val description: String,
    val waterAmountMl: Int,
    val timeRange: String
)
