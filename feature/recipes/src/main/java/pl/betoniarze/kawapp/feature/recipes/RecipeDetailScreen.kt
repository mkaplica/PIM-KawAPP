package pl.betoniarze.kawapp.feature.recipes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.theme.navbarColor
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerSection
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerState
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerViewModel
import pl.betoniarze.kawapp.feature.recipes.brew.components.BrewStartFab
import pl.betoniarze.kawapp.feature.recipes.brew.components.BrewStepsList
import pl.betoniarze.kawapp.feature.recipes.brew.components.BrewTimerBottomBar

@Composable
fun RecipeDetailScreen(
    modifier: Modifier = Modifier,
    recipeId: String = "",
    viewModel: RecipeDetailViewModel = hiltViewModel(),
    timerVm: BrewTimerViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onEditClick: (recipeId: String) -> Unit = {},
    onRecipeDeleted: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val timerState by timerVm.state.collectAsStateWithLifecycle()

    (uiState as? RecipeDetailUiState.Success)?.let { success ->
        BrewTimerSection(recipe = success.recipe, vm = timerVm)
    }

    RecipeDetailScreenContent(
        uiState = uiState,
        timerState = timerState,
        modifier = modifier,
        onBackClick = onBackClick,
        onEditClick = {
            (uiState as? RecipeDetailUiState.Success)?.let { onEditClick(it.recipe.id) }
        },
        onDeleteConfirm = { viewModel.deleteRecipe(onDeleted = onRecipeDeleted) },
        onStartTimer = timerVm::start,
        onToggleTimer = timerVm::toggle,
        onResetTimer = timerVm::reset,
        onSkipStep = timerVm::skipToNextStep,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreenContent(
    modifier: Modifier = Modifier,
    uiState: RecipeDetailUiState,
    timerState: BrewTimerState = BrewTimerState(),
    onBackClick: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteConfirm: () -> Unit = {},
    onStartTimer: () -> Unit = {},
    onToggleTimer: () -> Unit = {},
    onResetTimer: () -> Unit = {},
    onSkipStep: () -> Unit = {},
) {
    val recipeNotFoundText = stringResource(R.string.recipe_not_found)
    val backText = stringResource(R.string.back)
    val descriptionLabel = stringResource(R.string.recipe_description)
    val methodLabel = stringResource(R.string.brew_method)
    val proportionsLabel = stringResource(R.string.proportions)
    val stepsLabel = stringResource(R.string.recipe_steps)

    val isRecipeLoaded = uiState is RecipeDetailUiState.Success

    val wantFab = isRecipeLoaded && timerState.isIdle && timerState.hasSteps
    val wantBar = isRecipeLoaded && !timerState.isIdle

    val fabState = remember { MutableTransitionState(false) }
    val barState = remember { MutableTransitionState(false) }

    fabState.targetState = wantFab && barState.isIdle && !barState.currentState
    barState.targetState = wantBar && fabState.isIdle && !fabState.currentState

    // Editing or deleting mid-brew would pull the recipe out from under the timer.
    // Built-in recipes are not in the database, so they cannot be edited or deleted.
    val showRecipeActions = uiState is RecipeDetailUiState.Success &&
            !uiState.recipe.isPredefined &&
            timerState.isIdle
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    if (showDeleteDialog && uiState is RecipeDetailUiState.Success) {
        DeleteRecipeDialog(
            recipeTitle = uiState.recipe.title,
            onConfirm = {
                showDeleteDialog = false
                onDeleteConfirm()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = backText)
                    }
                },
                actions = {
                    if (showRecipeActions) {
                        IconButton(onClick = onEditClick) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = stringResource(R.string.edit_recipe)
                            )
                        }
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(R.string.delete_recipe)
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visibleState = fabState,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
            ) {
                BrewStartFab(onClick = onStartTimer)
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visibleState = barState,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut(),
            ) {
                BrewTimerBottomBar(
                    state = timerState,
                    onToggle = onToggleTimer,
                    onReset = onResetTimer,
                    onSkip = onSkipStep,
                )
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.navbarColor)
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is RecipeDetailUiState.Loading -> CircularProgressIndicator()
                is RecipeDetailUiState.NotFound -> Text(
                    text = recipeNotFoundText,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.error
                )

                is RecipeDetailUiState.Success -> {
                    val recipe = uiState.recipe
                    val proportionsText = stringResource(
                        R.string.recipe_proportions_format,
                        recipe.coffeeGrams,
                        recipe.totalWaterMl
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = recipe.title,
                                style = MaterialTheme.typography.titleLarge
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(descriptionLabel)
                                        append("\n")
                                    }
                                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                                        append(recipe.notes)
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(methodLabel)
                                        append(" ")
                                    }
                                    append(recipe.brewMethod.displayName)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                        append(proportionsLabel)
                                        append(" ")
                                    }
                                    append(proportionsText)
                                },
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.padding(16.dp))

                        Text(
                            text = stepsLabel,
                            fontWeight = FontWeight.Bold,
                        )
                        BrewStepsList(steps = recipe.steps, timerState = timerState)

                        Spacer(modifier = Modifier.padding(40.dp))

                    }
                }
            }
        }
    }
}
@Composable
private fun DeleteRecipeDialog(
    recipeTitle: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(R.string.delete_recipe_confirm_title)) },
        text = { Text(text = stringResource(R.string.delete_recipe_confirm_text, recipeTitle)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = stringResource(R.string.delete),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}
