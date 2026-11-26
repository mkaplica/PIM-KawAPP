package pl.betoniarze.kawapp.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.betoniarze.kawapp.core.model.BrewLog
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.CoffeeBag
import pl.betoniarze.kawapp.core.model.Grinder
import java.time.LocalDate

/** Room row for a [BrewLog]. The coffee bag is embedded as columns of the same table. */
@Entity(tableName = "brew_logs")
data class BrewLogEntity(
    @PrimaryKey val id: String,
    val brewMethodId: String,
    val brewMethodName: String,
    val date: LocalDate,
    val time: String,
    @Embedded val coffeeBag: CoffeeBagEntity,
    val recipeId: String,
    val recipeTitle: String,
    val grinderId: String?,
    val grinderName: String?,
    val grindSetting: String,
    val tastingNotes: String,
    val rating: Int
)

/** Coffee bag columns embedded in [BrewLogEntity]. */
data class CoffeeBagEntity(
    val roasterName: String,
    val beanName: String,
    val origin: String,
    val roastDate: LocalDate
)

/** Maps the embedded entity to the domain [CoffeeBag]. */
fun CoffeeBagEntity.asExternalModel() = CoffeeBag(
    roasterName = roasterName,
    beanName = beanName,
    origin = origin,
    roastDate = roastDate
)

/** Maps the row to the domain [BrewLog]. */
fun BrewLogEntity.asExternalModel() = BrewLog(
    id = id,
    brewMethod = BrewMethod(brewMethodId, brewMethodName),
    date = date,
    time = time,
    coffeeBag = coffeeBag.asExternalModel(),
    recipeId = recipeId,
    recipeTitle = recipeTitle,
    grinder = if (grinderId != null && grinderName != null) Grinder(grinderId, grinderName) else null,
    grindSetting = grindSetting,
    tastingNotes = tastingNotes,
    rating = rating
)

fun CoffeeBag.asEntity() = CoffeeBagEntity(
    roasterName = roasterName,
    beanName = beanName,
    origin = origin,
    roastDate = roastDate
)

fun BrewLog.asEntity() = BrewLogEntity(
    id = id,
    brewMethodId = brewMethod.id,
    brewMethodName = brewMethod.displayName,
    date = date,
    time = time,
    coffeeBag = coffeeBag.asEntity(),
    recipeId = recipeId,
    recipeTitle = recipeTitle,
    grinderId = grinder?.id,
    grinderName = grinder?.displayName,
    grindSetting = grindSetting,
    tastingNotes = tastingNotes,
    rating = rating
)