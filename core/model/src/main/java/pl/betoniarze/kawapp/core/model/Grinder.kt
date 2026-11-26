package pl.betoniarze.kawapp.core.model

/**
 * A grinder the user added in the Profile. Brew logs keep their own copy of [id] and
 * [displayName], so a log still reads well after the grinder is deleted.
 */
data class Grinder(
    val id: String,
    val displayName: String,
) {
    companion object {
        /** Sample grinders used by the debug brew logs in FakeCoffeeData. */
        val COMANDANTE_C40 = Grinder(id = "COMANDANTE_C40", displayName = "Comandante C40")
        val NICHE_ZERO = Grinder(id = "NICHE_ZERO", displayName = "Niche Zero")
    }
}

