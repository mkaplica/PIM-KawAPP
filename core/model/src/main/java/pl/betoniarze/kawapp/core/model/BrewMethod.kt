package pl.betoniarze.kawapp.core.model

/** Supported brewing devices. [displayName] is the product name shown in the UI. */
enum class BrewMethod(val displayName: String) {
    V60("Hario V60"),
    AEROPRESS("AeroPress")
}
