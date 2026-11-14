package pl.betoniarze.kawapp.feature.recipes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.ui.R

@Composable
fun RecipeAddScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    viewModel: RecipeAddScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    RecipeAddScreenContent(
        uiState = uiState,
        modifier = modifier,
        onBackClick = onBackClick,
        onSaveClick = viewModel::saveRecipe,
        onTitleChange = viewModel::onTitleChange,
        onBrewMethodChange = viewModel::onBrewMethodChange,
        onCoffeeGramsChange = viewModel::onCoffeeGramsChange,
        onTotalWaterMlChange = viewModel::onTotalWaterMlChange,
        onNotesChange = viewModel::onNotesChange,
        onAddStep = viewModel::onAddStep,
        onRemoveStep = viewModel::onRemoveStep,
        onStepChange = viewModel::onStepChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeAddScreenContent(
    modifier: Modifier = Modifier,
    uiState: RecipeAddUiState,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onTitleChange: (String) -> Unit = {},
    onBrewMethodChange: (BrewMethod) -> Unit = {},
    onCoffeeGramsChange: (String) -> Unit = {},
    onTotalWaterMlChange: (String) -> Unit = {},
    onNotesChange: (String) -> Unit = {},
    onAddStep: () -> Unit = {},
    onRemoveStep: (Int) -> Unit = {},
    onStepChange: (Int, BrewStepInput) -> Unit = { _, _ -> }
) {
    LaunchedEffect(uiState.isSaveSuccess) {
        if (uiState.isSaveSuccess) {
            onBackClick()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.new_recipe)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                actions = {
                    TextButton(
                        onClick = onSaveClick,
                        enabled = uiState.isValid && !uiState.isSaving
                    ) {
                        Text(text = stringResource(R.string.save))
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = onTitleChange,
                    label = { Text(text = stringResource(R.string.name)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    BrewMethod.entries.forEach { method ->
                        FilterChip(
                            selected = uiState.brewMethod == method,
                            onClick = { onBrewMethodChange(method) },
                            label = { Text(method.displayName) }
                        )
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.coffeeGrams,
                        onValueChange = onCoffeeGramsChange,
                        label = { Text(text = stringResource(R.string.coffee_grams)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = uiState.totalWaterMl,
                        onValueChange = onTotalWaterMlChange,
                        label = { Text(text = stringResource(R.string.water_ml))},
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = uiState.notes,
                    onValueChange = onNotesChange,
                    label = { Text(text = stringResource(R.string.notes)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2
                )
            }

            item {
                Text(
                    text = stringResource(R.string.brew_steps),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            itemsIndexed(uiState.steps) { index, step ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(stringResource(R.string.step) + " ${step.stepNumber}", style = MaterialTheme.typography.labelLarge)
                            if (uiState.steps.size > 1) {
                                IconButton(onClick = { onRemoveStep(index) }) {
                                    Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete_step))
                                }
                            }
                        }

                        OutlinedTextField(
                            value = step.description,
                            onValueChange = { onStepChange(index, step.copy(description = it)) },
                            label = { Text(text = stringResource(R.string.description)) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = step.waterAmountMl,
                                onValueChange = {
                                    if (it.all { c -> c.isDigit() }) {
                                        onStepChange(index, step.copy(waterAmountMl = it))
                                    }
                                },
                                label = { Text(text = stringResource(R.string.water_ml)) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                            OutlinedTextField(
                                value = step.timeRange,
                                onValueChange = { onStepChange(index, step.copy(timeRange = it)) },
                                label = {
                                    Text (
                                        text = stringResource(R.string.time),
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = true
                            )
                        }
                    }
                }
            }

            item {
                OutlinedButton(
                    onClick = onAddStep,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(text = stringResource(R.string.add_step))
                }
            }
        }
    }
}