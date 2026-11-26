package pl.betoniarze.kawapp.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.Grinder

/**
 * Room row for a [BrewMethod] the user added in the Profile.
 *
 * @property createdAt Epoch millis; brewMethods are listed in the order they were added.
 */
@Entity(tableName = "brew_methods")
data class BrewMethodEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val createdAt: Long,
)

/** Maps the row to the domain [BrewMethod]. */
fun BrewMethodEntity.asExternalModel() = BrewMethod(
    id = id,
    displayName = displayName,
)

/** Maps [BrewMethod] to a row, stamped with [createdAt]. */
fun BrewMethod.asEntity(createdAt: Long = System.currentTimeMillis()) = BrewMethodEntity(
    id = id,
    displayName = displayName,
    createdAt = createdAt,
)
