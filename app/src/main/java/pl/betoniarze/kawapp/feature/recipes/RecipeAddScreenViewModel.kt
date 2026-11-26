package pl.betoniarze.kawapp.feature.recipes

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.data.repository.BrewMethodRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.BrewStep
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import java.util.UUID
import javax.inject.Inject

/**
 * State of the recipe form. Numbers are kept as text so the fields can be empty while typing.
 *
 * @property isSaveSuccess Set after saving; the screen then navigates back.
 * @property isEditMode True when editing an existing recipe instead of adding a new one.
 * @property isLoading True while the recipe to edit is being loaded.
 * @property brewMethods Built-in and Profile methods offered as chips.
 */
data class RecipeAddUiState(
    val title: String = "",
    val brewMethod: BrewMethod = BrewMethod.V60,
    val coffeeGrams: String = "",
    val totalWaterMl: String = "",
    val notes: String = "",
    val steps: List<BrewStepInput> = listOf(BrewStepInput(stepNumber = 1)),
    val isSaving: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isEditMode: Boolean = false,
    val isLoading: Boolean = false,
    val brewMethods: List<BrewMethod> = BrewMethod.BUILT_IN,
) {
    /**
     * Chips to show: [brewMethods], plus the edited recipe's method if it was deleted from the
     * Profile in the meantime, so the current choice is always visible.
     */
    val methodOptions: List<BrewMethod>
        get() = if (brewMethods.any { it.id == brewMethod.id }) brewMethods else brewMethods + brewMethod

    /**
     * Whether the recipe can be saved: a title, a dose and water above 0, and every step described.
     */
    val isValid: Boolean
        get() = title.isNotBlank() &&
                (coffeeGrams.toIntOrNull() ?: 0) > 0 &&
                (totalWaterMl.toIntOrNull() ?: 0) > 0 &&
                steps.isNotEmpty() &&
                steps.all { it.description.isNotBlank() }
}

/** One step as entered in the form; numbers are kept as text. */
data class BrewStepInput(
    val stepNumber: Int,
    val description: String = "",
    val waterAmountMl: String = "",
    val timeRange: String = ""
)

/** ViewModel of the add / edit recipe form. */
@HiltViewModel
class RecipeAddScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val brewMethodRepository: BrewMethodRepository,
) : ViewModel() {
    // Present only when opened via the edit_recipe/{recipeId} route.
    private val editedRecipeId: String? = savedStateHandle.get<String>("recipeId")

    private val _uiState = MutableStateFlow(
        RecipeAddUiState(isEditMode = editedRecipeId != null, isLoading = editedRecipeId != null)
    )
    val uiState: StateFlow<RecipeAddUiState> = _uiState

    init {
        editedRecipeId?.let(::loadRecipe)
        observeBrewMethods()
    }

    /** Keeps the method chips in sync with the Profile. */
    private fun observeBrewMethods() {
        viewModelScope.launch {
            brewMethodRepository.getBrewMethods().collect { methods ->
                _uiState.update { it.copy(brewMethods = methods) }
            }
        }
    }

    /** Fills the form with the recipe [id] being edited. */
    private fun loadRecipe(id: String) {
        viewModelScope.launch {
            val recipe = recipeRepository.getRecipeById(id).first()
            _uiState.update { state ->
                if (recipe == null) {
                    state.copy(isLoading = false)
                } else {
                    state.copy(
                        title = recipe.title,
                        brewMethod = recipe.brewMethod,
                        coffeeGrams = recipe.coffeeGrams.toString(),
                        totalWaterMl = recipe.totalWaterMl.toString(),
                        notes = recipe.notes,
                        steps = recipe.steps.map { step ->
                            BrewStepInput(
                                stepNumber = step.stepNumber,
                                description = step.description,
                                waterAmountMl = step.waterAmountMl.toString(),
                                timeRange = step.timeRange
                            )
                        }.ifEmpty { listOf(BrewStepInput(stepNumber = 1)) },
                        isLoading = false
                    )
                }
            }
        }
    }

    /** Updates the recipe title. */
    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    /** Updates the brew method. */
    fun onBrewMethodChange(brewMethod: BrewMethod) {
        _uiState.update { it.copy(brewMethod = brewMethod) }
    }

    /** Updates the coffee dose; input that is not only digits is ignored. */
    fun onCoffeeGramsChange(coffeeGrams: String) {
        if (coffeeGrams.all { it.isDigit() }) {
            _uiState.update { it.copy(coffeeGrams = coffeeGrams) }
        }
    }

    /** Updates the total water; input that is not only digits is ignored. */
    fun onTotalWaterMlChange(totalWaterMl: String) {
        if (totalWaterMl.all { it.isDigit() }) {
            _uiState.update { it.copy(totalWaterMl = totalWaterMl) }
        }
    }

    /** Updates the recipe notes. */
    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    /** Adds an empty step at the end. */
    fun onAddStep() {
        _uiState.update { state ->
            val nextNumber = state.steps.size + 1
            state.copy(steps = state.steps + BrewStepInput(stepNumber = nextNumber))
        }
    }

    /**
     * Removes the step at [index] and renumbers the rest. The last remaining step cannot be
     * removed.
     */
    fun onRemoveStep(index: Int) {
        _uiState.update { state ->
            if (state.steps.size <= 1) return@update state // Keep at least 1 step
            val updatedSteps = state.steps
                .filterIndexed { i, _ -> i != index }
                .mapIndexed { i, step -> step.copy(stepNumber = i + 1) } // Renumber the steps
            state.copy(steps = updatedSteps)
        }
    }

    /** Replaces the step at [index] with [updatedStep]. */
    fun onStepChange(index: Int, updatedStep: BrewStepInput) {
        _uiState.update { state ->
            val updatedSteps = state.steps.mapIndexed { i, step ->
                if (i == index) updatedStep else step
            }
            state.copy(steps = updatedSteps)
        }
    }

    /**
     * Saves the recipe (new, or overwriting the edited one) if the form is valid. Text is trimmed
     * and empty step water becomes 0.
     */
    fun saveRecipe() {
        val currentState = _uiState.value
        if (!currentState.isValid || currentState.isSaving || currentState.isLoading) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val recipe = CoffeeRecipe(
                id = editedRecipeId ?: UUID.randomUUID().toString(),
                title = currentState.title.trim(),
                brewMethod = currentState.brewMethod,
                coffeeGrams = currentState.coffeeGrams.toInt(),
                totalWaterMl = currentState.totalWaterMl.toInt(),
                notes = currentState.notes.trim(),
                steps = currentState.steps.map { stepInput ->
                    BrewStep(
                        stepNumber = stepInput.stepNumber,
                        description = stepInput.description.trim(),
                        waterAmountMl = stepInput.waterAmountMl.toIntOrNull() ?: 0,
                        timeRange = stepInput.timeRange.trim()
                    )
                }
            )

            if (editedRecipeId != null) {
                recipeRepository.updateRecipe(recipe)
            } else {
                recipeRepository.addRecipe(recipe)
            }
            _uiState.update { it.copy(isSaving = false, isSaveSuccess = true) }
        }
    }
}
