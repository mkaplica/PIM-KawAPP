package pl.betoniarze.kawapp.core.model

data class BrewStep(
    val stepNumber: Int,
    val description: String,
    val waterAmountMl: Int,
    val timeRange: String   // ex. "0:00 - 0:45"
)
