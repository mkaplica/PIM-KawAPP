package pl.betoniarze.kawapp.core.model

/** App theme chosen in Profile. [SYSTEM] follows the phone's dark mode setting. */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK;

    companion object {
        /**
         * Returns the mode saved under [name], or [SYSTEM] when nothing (or an unknown value) was
         * saved.
         */
        fun fromName(name: String?): ThemeMode = entries.find { it.name == name } ?: SYSTEM
    }
}
