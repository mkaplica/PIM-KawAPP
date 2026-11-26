package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.model.Grinder

/** The user's own grinders, managed in the Profile. */
interface GrinderRepository {
    /** All grinders in the order they were added. Emits again when they change. */
    fun getGrinders(): Flow<List<Grinder>>

    /** Adds [grinder], or replaces one with the same id. */
    suspend fun addGrinder(grinder: Grinder)

    /** Deletes the grinder with [id]. Brew logs keep their own copy of it. */
    suspend fun deleteGrinder(id: String)
}
