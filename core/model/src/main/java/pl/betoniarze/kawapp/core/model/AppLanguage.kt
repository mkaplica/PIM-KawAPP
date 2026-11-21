package pl.betoniarze.kawapp.core.model

/**
 * Languages the app UI can be shown in.
 *
 * @property code BCP 47 language tag passed to AppCompat, e.g. "pl".
 * @property displayName Name of the language in that language.
 */
enum class AppLanguage(
    val code: String,
    val displayName: String
) {
    POLISH("pl", "Polski"),
    ENGLISH("en", "English");

    companion object {
        /**
         * Returns the language for [code] (case-insensitive), or [POLISH] when it is not supported.
         */
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: POLISH
        }
    }
}
