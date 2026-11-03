package pl.betoniarze.kawapp.core.model

enum class AppLanguage(
    val code: String,
    val displayName: String
) {
    POLISH("pl", "Polski"),
    ENGLISH("en", "English");

    companion object {
        fun fromCode(code: String): AppLanguage {
            return entries.find { it.code.equals(code, ignoreCase = true) } ?: POLISH
        }
    }
}
