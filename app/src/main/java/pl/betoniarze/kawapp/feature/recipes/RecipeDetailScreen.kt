package pl.betoniarze.kawapp.feature.recipes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawLoadingOverlay
import pl.betoniarze.kawapp.core.ui.components.KawFormat
import pl.betoniarze.kawapp.core.ui.components.KawStat
import pl.betoniarze.kawapp.core.ui.components.KawStatTiles
import pl.betoniarze.kawapp.core.ui.components.KawTag
import pl.betoniarze.kawapp.core.ui.components.KawTagDefaults
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerSection
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerState
import pl.betoniarze.kawapp.feature.recipes.brew.BrewTimerViewModel
import pl.betoniarze.kawapp.feature.recipes.brew.components.BrewStartFab
import pl.betoniarze.kawapp.feature.recipes.brew.components.BrewStepsList
import pl.betoniarze.kawapp.feature.recipes.brew.components.BrewTimerBottomBar
import pl.betoniarze.kawapp.feature.recipes.brew.toTimedSteps

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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = {},
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent,
                    ),
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
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                when (uiState) {
                    is RecipeDetailUiState.Loading -> Unit
                    is RecipeDetailUiState.NotFound -> Text(
                        text = stringResource(R.string.recipe_not_found),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.error
                    )

                    is RecipeDetailUiState.Success -> RecipeDetailBody(
                        recipe = uiState.recipe,
                        timerState = timerState,
                    )
                }
            }
        }

        KawLoadingOverlay(visible = uiState is RecipeDetailUiState.Loading)
    }
}

@Composable
private fun RecipeDetailBody(
    recipe: CoffeeRecipe,
    timerState: BrewTimerState,
) {
    val totalSeconds = remember(recipe) { recipe.toTimedSteps().maxOfOrNull { it.endSec } }
    val stats = buildList {
        add(KawStat("${recipe.coffeeGrams} g", stringResource(R.string.stat_coffee)))
        add(KawStat("${recipe.totalWaterMl} ml", stringResource(R.string.stat_water)))
        KawFormat.ratio(recipe.coffeeGrams, recipe.totalWaterMl)?.let {
            add(KawStat(it, stringResource(R.string.stat_ratio)))
        }
        totalSeconds?.let { add(KawStat(KawFormat.duration(it), stringResource(R.string.stat_time))) }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Hero – chip metody neutralny: miód na tym ekranie ma tylko aktywny krok.
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            KawTag(text = recipe.brewMethod.displayName, colors = KawTagDefaults.neutral())
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp),
            )
            if (recipe.notes.isNotBlank()) {
                Text(
                    text = recipe.notes,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        KawStatTiles(stats = stats)

        Text(
            text = stringResource(R.string.recipe_steps),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
        )
        BrewStepsList(steps = recipe.steps, timerState = timerState)

        // Keep the last step clear of the start FAB.
        Spacer(Modifier.height(72.dp))
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
