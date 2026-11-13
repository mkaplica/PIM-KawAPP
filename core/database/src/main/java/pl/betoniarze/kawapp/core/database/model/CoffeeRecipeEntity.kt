package pl.betoniarze.kawapp.core.database.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Index
import androidx.room.Relation
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.BrewStep
import pl.betoniarze.kawapp.core.model.CoffeeRecipe

@Entity(tableName = "coffee_recipes")
data class CoffeeRecipeEntity (
    @PrimaryKey val id: String,
    val title: String,
    val brewMethod: BrewMethod,
    val coffeeGrams: Int,
    val totalWaterMl: Int,
    val notes: String
)

@Entity(
    tableName = "brew_steps",
    foreignKeys = [
        ForeignKey(
            entity = CoffeeRecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("recipeId")]
)
data class BrewStepEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val recipeId: String,
    val stepNumber: Int,
    val description: String,
    val waterAmountMl: Int,
    val timeRange: String
)

data class CoffeeRecipeWithSteps(
    @Embedded val recipe: CoffeeRecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val steps: List<BrewStepEntity>
)

fun BrewStepEntity.asExternalModel() = BrewStep(
    stepNumber = stepNumber,
    description = description,
    waterAmountMl = waterAmountMl,
    timeRange = timeRange
)

fun CoffeeRecipeWithSteps.asExternalModel() = CoffeeRecipe(
    id = recipe.id,
    title = recipe.title,
    brewMethod = recipe.brewMethod,
    coffeeGrams = recipe.coffeeGrams,
    totalWaterMl = recipe.totalWaterMl,
    notes = recipe.notes,
    steps = steps
        .sortedBy { it.stepNumber }
        .map { it.asExternalModel() }
)