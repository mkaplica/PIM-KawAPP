package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.model.BrewMethod

/** The user's own brew methods, managed in the Profile. */
interface BrewMethodRepository {
    /** All brew methods in the order they were added. Emits again when they change. */
    fun getBrewMethods(): Flow<List<BrewMethod>>

    /** Adds [brewMethod], or replaces one with the same id. */
    suspend fun addBrewMethod(brewMethod: BrewMethod)

    /** Deletes the brew method with [id]. Brew logs keep their own copy of it. */
    suspend fun deleteBrewMethod(id: String)
}
