package pl.betoniarze.kawapp.feature.journal.save

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.LocalDate
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.CoffeeBag
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.model.Grinder
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawCard
import pl.betoniarze.kawapp.core.ui.components.KawFormat
import pl.betoniarze.kawapp.core.ui.components.KawLoadingOverlay
import pl.betoniarze.kawapp.core.ui.components.KawStat
import pl.betoniarze.kawapp.core.ui.components.KawStatTiles
import pl.betoniarze.kawapp.core.ui.components.KawTag
import pl.betoniarze.kawapp.core.ui.components.KawTagDefaults
import pl.betoniarze.kawapp.feature.recipes.brew.toTimedSteps
import pl.betoniarze.kawapp.ui.components.KawFieldShape
import pl.betoniarze.kawapp.ui.components.MAX_RATING
import pl.betoniarze.kawapp.ui.components.RatingPicker
import pl.betoniarze.kawapp.ui.components.RoastDateField

/**
 * "Save brew" form connected to [SaveBrewViewModel]: rating, coffee, grind and a description.
 * Calls [onSaved] once the brew is in the Journal and on the Feed.
 */
@Composable
fun SaveBrewScreen(
    onBackClick: () -> Unit,
    onSaved: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SaveBrewViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) onSaved()
    }

    SaveBrewScreenContent(
        uiState = uiState,
        modifier = modifier,
        onBackClick = onBackClick,
        onSaveClick = viewModel::saveBrew,
        onRatingChange = viewModel::onRatingChange,
        onBrewMethodChange = viewModel::onBrewMethodChange,
        onCoffeeBagSelected = viewModel::onCoffeeBagSelected,
        onRoasterNameChange = viewModel::onRoasterNameChange,
        onBeanNameChange = viewModel::onBeanNameChange,
        onOriginChange = viewModel::onOriginChange,
        onRoastDateChange = viewModel::onRoastDateChange,
        onGrinderChange = viewModel::onGrinderChange,
        onGrindSettingChange = viewModel::onGrindSettingChange,
        onDescriptionChange = viewModel::onDescriptionChange,
    )
}

/**
 * Stateless "save brew" UI. The rating card is the one honey element on this screen; everything
 * else uses the same neutral fields and chips as the recipe form.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SaveBrewScreenContent(
    uiState: SaveBrewUiState,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSaveClick: (autoDescription: String) -> Unit = {},
    onRatingChange: (Int) -> Unit = {},
    onBrewMethodChange: (BrewMethod) -> Unit = {},
    onCoffeeBagSelected: (CoffeeBag) -> Unit = {},
    onRoasterNameChange: (String) -> Unit = {},
    onBeanNameChange: (String) -> Unit = {},
    onOriginChange: (String) -> Unit = {},
    onRoastDateChange: (LocalDate) -> Unit = {},
    onGrinderChange: (Grinder) -> Unit = {},
    onGrindSettingChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {},
) {
    val autoDescription = buildAutoDescription(uiState)

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(
                    title = { Text(text = stringResource(R.string.save_brew_title)) },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.back),
                            )
                        }
                    },
                )
            },
            bottomBar = {
                SaveBrewBar(uiState = uiState, onSaveClick = { onSaveClick(autoDescription) })
            },
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 4.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                item {
                    val recipe = uiState.recipe
                    if (recipe != null) {
                        RecipeHero(recipe = recipe)
                    } else {
                        MethodSection(
                            methods = uiState.brewMethods,
                            selected = uiState.brewMethod,
                            onSelect = onBrewMethodChange,
                        )
                    }
                }

                item {
                    RatingCard(rating = uiState.rating, onRatingChange = onRatingChange)
                }

                item {
                    DescriptionField(
                        value = uiState.description,
                        autoDescription = autoDescription,
                        onValueChange = onDescriptionChange,
                    )
                }

                item {
                    CoffeeSection(
                        uiState = uiState,
                        onCoffeeBagSelected = onCoffeeBagSelected,
                        onRoasterNameChange = onRoasterNameChange,
                        onBeanNameChange = onBeanNameChange,
                        onOriginChange = onOriginChange,
                        onRoastDateChange = onRoastDateChange,
                    )
                }

                item {
                    GrindSection(
                        grinders = uiState.grinders,
                        grinder = uiState.grinder,
                        grindSetting = uiState.grindSetting,
                        onGrinderChange = onGrinderChange,
                        onGrindSettingChange = onGrindSettingChange,
                    )
                }
            }
        }

        KawLoadingOverlay(visible = uiState.isLoading)
    }
}

/** Same hero as the recipe detail: neutral method chip, title and the recipe's stat tiles. */
@Composable
private fun RecipeHero(recipe: CoffeeRecipe) {
    val totalSeconds = remember(recipe) { recipe.toTimedSteps().maxOfOrNull { it.endSec } }
    val stats = buildList {
        add(KawStat("${recipe.coffeeGrams} g", stringResource(R.string.stat_coffee)))
        add(KawStat("${recipe.totalWaterMl} ml", stringResource(R.string.stat_water)))
        KawFormat.ratio(recipe.coffeeGrams, recipe.totalWaterMl)?.let {
            add(KawStat(it, stringResource(R.string.stat_ratio)))
        }
        totalSeconds?.let { add(KawStat(KawFormat.duration(it), stringResource(R.string.stat_time))) }
    }

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            KawTag(text = recipe.brewMethod.displayName, colors = KawTagDefaults.neutral())
            Text(
                text = recipe.title,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp),
            )
        }
        KawStatTiles(stats = stats)
    }
}

/** Method choice, shown only when the brew is not tied to a recipe (opened from the Journal). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun MethodSection(
    methods: List<BrewMethod>,
    selected: BrewMethod,
    onSelect: (BrewMethod) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(text = stringResource(R.string.section_method))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            methods.forEach { method ->
                FilterChip(
                    selected = selected.id == method.id,
                    onClick = { onSelect(method) },
                    label = { Text(method.displayName) },
                )
            }
        }
    }
}

/**
 * The focal point of the screen: a large "7 / 10" set like the brew timer's "elapsed / total",
 * above the star row.
 */
@Composable
private fun RatingCard(rating: Int, onRatingChange: (Int) -> Unit) {
    KawCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Bottom,
        ) {
            Text(
                text = stringResource(R.string.rating_title),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 6.dp),
            )
            Text(
                text = buildAnnotatedString {
                    append(if (rating == 0) "–" else rating.toString())
                    withStyle(
                        SpanStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    ) {
                        append(" / $MAX_RATING")
                    }
                },
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Medium,
                    fontFeatureSettings = "tnum",
                ),
            )
        }

        RatingPicker(rating = rating, onRatingChange = onRatingChange)

        AnimatedVisibility(visible = rating == 0) {
            Text(
                text = stringResource(R.string.rating_hint),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** Coffee fields with one-tap picks from earlier brews. */
@Composable
private fun CoffeeSection(
    uiState: SaveBrewUiState,
    onCoffeeBagSelected: (CoffeeBag) -> Unit,
    onRoasterNameChange: (String) -> Unit,
    onBeanNameChange: (String) -> Unit,
    onOriginChange: (String) -> Unit,
    onRoastDateChange: (LocalDate) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(text = stringResource(R.string.section_coffee))

        if (uiState.recentBags.isNotEmpty()) {
            Text(
                text = stringResource(R.string.recent_coffees),
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(uiState.recentBags) { bag ->
                    FilterChip(
                        selected = uiState.isSelected(bag),
                        onClick = { onCoffeeBagSelected(bag) },
                        label = { Text("${bag.origin} ${bag.beanName}".trim()) },
                    )
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FormField(
                value = uiState.roasterName,
                onValueChange = onRoasterNameChange,
                label = stringResource(R.string.roaster_name),
                modifier = Modifier.weight(1f),
            )
            FormField(
                value = uiState.beanName,
                onValueChange = onBeanNameChange,
                label = stringResource(R.string.bean_name),
                modifier = Modifier.weight(1f),
            )
        }
        FormField(
            value = uiState.origin,
            onValueChange = onOriginChange,
            label = stringResource(R.string.coffee_origin),
            modifier = Modifier.fillMaxWidth(),
        )
        RoastDateField(
            date = uiState.roastDate,
            onDateChange = onRoastDateChange,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/** Grinder chips from the Profile, or a hint to add one there when the user has none. */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GrindSection(
    grinders: List<Grinder>,
    grinder: Grinder?,
    grindSetting: String,
    onGrinderChange: (Grinder) -> Unit,
    onGrindSettingChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(text = stringResource(R.string.section_grind))
        if (grinders.isEmpty()) {
            Text(
                text = stringResource(R.string.save_brew_no_grinders),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                grinders.forEach { entry ->
                    FilterChip(
                        selected = grinder?.id == entry.id,
                        onClick = { onGrinderChange(entry) },
                        label = { Text(entry.displayName) },
                    )
                }
            }
        }
        FormField(
            value = grindSetting,
            onValueChange = onGrindSettingChange,
            label = stringResource(R.string.grind_setting),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

/**
 * Bottom bar in the timer bar's style. While the form is incomplete it says what is missing,
 * instead of leaving a silently disabled button.
 */
@Composable
private fun SaveBrewBar(uiState: SaveBrewUiState, onSaveClick: () -> Unit) {
    val hint = when {
        uiState.isLoading -> null
        !uiState.hasRating -> stringResource(R.string.save_brew_hint_rating)
        !uiState.hasCoffee -> stringResource(R.string.save_brew_hint_coffee)
        else -> null
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding()
                .padding(start = 12.dp, end = 12.dp, top = 10.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            AnimatedVisibility(visible = hint != null) {
                Text(
                    text = hint.orEmpty(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Button(
                onClick = onSaveClick,
                enabled = uiState.canSave,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
            ) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                )
                Spacer(Modifier.width(ButtonDefaults.IconSpacing))
                Text(
                    text = stringResource(R.string.save_brew),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

/**
 * Description field. While it is empty, the supporting text previews the summary that will be
 * posted instead, so nobody is surprised by text they did not write.
 */
@Composable
private fun DescriptionField(
    value: String,
    autoDescription: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = stringResource(R.string.brew_description)) },
        placeholder = { Text(text = stringResource(R.string.brew_description_hint)) },
        supportingText = if (value.isBlank()) {
            { Text(text = stringResource(R.string.brew_description_auto_preview, autoDescription)) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        shape = KawFieldShape,
        minLines = 3,
        modifier = Modifier.fillMaxWidth(),
    )
}

/**
 * Summary used when the description is left empty, e.g. "Hario V60 według przepisu „Hoffmann”.
 * Kawa: Etiopia Guji z palarni Coffeelab. Ocena: 8/10." Parts the user has not filled in yet
 * (coffee, rating) are skipped.
 */
@Composable
private fun buildAutoDescription(uiState: SaveBrewUiState): String {
    val method = (uiState.recipe?.brewMethod ?: uiState.brewMethod).displayName
    val recipeTitle = uiState.recipe?.title
    val coffee = "${uiState.origin.trim()} ${uiState.beanName.trim()}".trim()
    val roaster = uiState.roasterName.trim()

    val parts = buildList {
        add(
            if (recipeTitle != null) stringResource(R.string.auto_desc_recipe, method, recipeTitle)
            else stringResource(R.string.auto_desc_method, method)
        )
        if (uiState.beanName.isNotBlank()) {
            add(
                if (roaster.isNotEmpty()) stringResource(R.string.auto_desc_coffee, coffee, roaster)
                else stringResource(R.string.auto_desc_coffee_no_roaster, coffee)
            )
        }
        if (uiState.hasRating) {
            add(stringResource(R.string.auto_desc_rating, uiState.rating, MAX_RATING))
        }
    }
    return parts.joinToString(separator = " ")
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Medium,
    )
}

/** Single-line outlined field with the app's 12 dp corners. */
@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
        shape = KawFieldShape,
        singleLine = true,
        modifier = modifier,
    )
}
