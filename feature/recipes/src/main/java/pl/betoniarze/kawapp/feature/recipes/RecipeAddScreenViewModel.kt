package pl.betoniarze.kawapp.feature.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.BrewStep
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import java.util.UUID
import javax.inject.Inject

data class RecipeAddUiState(
    val title: String = "",
    val brewMethod: BrewMethod = BrewMethod.V60,
    val coffeeGrams: String = "",
    val totalWaterMl: String = "",
    val notes: String = "",
    val steps: List<BrewStepInput> = listOf(BrewStepInput(stepNumber = 1)),
    val isSaving: Boolean = false,
    val isSaveSuccess: Boolean = false
) {
    val isValid: Boolean
        get() = title.isNotBlank() &&
                (coffeeGrams.toIntOrNull() ?: 0) > 0 &&
                (totalWaterMl.toIntOrNull() ?: 0) > 0 &&
                steps.isNotEmpty() &&
                steps.all { it.description.isNotBlank() }
}

data class BrewStepInput(
    val stepNumber: Int,
    val description: String = "",
    val waterAmountMl: String = "",
    val timeRange: String = ""
)

@HiltViewModel
class RecipeAddScreenViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecipeAddUiState())
    val uiState: StateFlow<RecipeAddUiState> = _uiState

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onBrewMethodChange(brewMethod: BrewMethod) {
        _uiState.update { it.copy(brewMethod = brewMethod) }
    }

    fun onCoffeeGramsChange(coffeeGrams: String) {
        if (coffeeGrams.all { it.isDigit() }) {
            _uiState.update { it.copy(coffeeGrams = coffeeGrams) }
        }
    }

    fun onTotalWaterMlChange(totalWaterMl: String) {
        if (totalWaterMl.all { it.isDigit() }) {
            _uiState.update { it.copy(totalWaterMl = totalWaterMl) }
        }
    }

    fun onNotesChange(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun onAddStep() {
        _uiState.update { state ->
            val nextNumber = state.steps.size + 1
            state.copy(steps = state.steps + BrewStepInput(stepNumber = nextNumber))
        }
    }

    fun onRemoveStep(index: Int) {
        _uiState.update { state ->
            if (state.steps.size <= 1) return@update state // Zostawiamy minimum 1 krok
            val updatedSteps = state.steps
                .filterIndexed { i, _ -> i != index }
                .mapIndexed { i, step -> step.copy(stepNumber = i + 1) } // Przenumerowanie kroków
            state.copy(steps = updatedSteps)
        }
    }

    fun onStepChange(index: Int, updatedStep: BrewStepInput) {
        _uiState.update { state ->
            val updatedSteps = state.steps.mapIndexed { i, step ->
                if (i == index) updatedStep else step
            }
            state.copy(steps = updatedSteps)
        }
    }

    fun saveRecipe() {
        val currentState = _uiState.value
        if (!currentState.isValid || currentState.isSaving) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val newRecipe = CoffeeRecipe(
                id = UUID.randomUUID().toString(),
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

            recipeRepository.addRecipe(newRecipe)
            _uiState.update { it.copy(isSaving = false, isSaveSuccess = true) }
        }
    }
}