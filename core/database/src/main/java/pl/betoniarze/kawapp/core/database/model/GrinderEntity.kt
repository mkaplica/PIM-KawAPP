package pl.betoniarze.kawapp.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.betoniarze.kawapp.core.model.Grinder

/**
 * Room row for a [Grinder] the user added in the Profile.
 *
 * @property createdAt Epoch millis; grinders are listed in the order they were added.
 */
@Entity(tableName = "grinders")
data class GrinderEntity(
    @PrimaryKey val id: String,
    val displayName: String,
    val createdAt: Long,
)

/** Maps the row to the domain [Grinder]. */
fun GrinderEntity.asExternalModel() = Grinder(
    id = id,
    displayName = displayName,
)

/** Maps [Grinder] to a row, stamped with [createdAt]. */
fun Grinder.asEntity(createdAt: Long = System.currentTimeMillis()) = GrinderEntity(
    id = id,
    displayName = displayName,
    createdAt = createdAt,
)
