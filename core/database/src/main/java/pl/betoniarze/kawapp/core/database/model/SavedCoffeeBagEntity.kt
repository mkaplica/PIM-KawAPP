package pl.betoniarze.kawapp.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import pl.betoniarze.kawapp.core.model.CoffeeBag

/**
 * Room row for a coffee the user keeps in the Profile ("Kawy"). Separate from [CoffeeBagEntity],
 * which is the copy embedded in each brew log.
 *
 * @property createdAt Epoch millis; the newest bag is listed first.
 */
@Entity(tableName = "coffee_bags")
data class SavedCoffeeBagEntity(
    @PrimaryKey val id: String,
    val roasterName: String,
    val beanName: String,
    val origin: String,
    val roastDate: LocalDate,
    val createdAt: Long,
)

/** Maps the row to the domain [CoffeeBag]. */
fun SavedCoffeeBagEntity.asExternalModel() = CoffeeBag(
    id = id,
    roasterName = roasterName,
    beanName = beanName,
    origin = origin,
    roastDate = roastDate,
)

/** Maps [CoffeeBag] to a saved-coffee row, stamped with [createdAt]. */
fun CoffeeBag.asSavedEntity(createdAt: Long = System.currentTimeMillis()) = SavedCoffeeBagEntity(
    id = id,
    roasterName = roasterName,
    beanName = beanName,
    origin = origin,
    roastDate = roastDate,
    createdAt = createdAt,
)
