package pl.betoniarze.kawapp.core.data.repository

import kotlinx.coroutines.flow.Flow
import pl.betoniarze.kawapp.core.model.CoffeeBag

/** Coffees the user keeps in the Profile, offered as quick picks when saving a brew. */
interface CoffeeBagRepository {
    /** All saved coffees, newest first. Emits again when they change. */
    fun getCoffeeBags(): Flow<List<CoffeeBag>>

    /** Adds [coffeeBag] (its id must be set), or replaces one with the same id. */
    suspend fun addCoffeeBag(coffeeBag: CoffeeBag)

    /** Deletes the coffee with [id]. Brew logs keep their own copy of it. */
    suspend fun deleteCoffeeBag(id: String)
}
