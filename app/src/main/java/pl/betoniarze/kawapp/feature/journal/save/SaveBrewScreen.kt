package pl.betoniarze.kawapp.feature.journal.save

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.ConfigurationCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
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
import pl.betoniarze.kawapp.ui.components.MAX_RATING
import pl.betoniarze.kawapp.ui.components.RatingPicker

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
    onSaveClick: () -> Unit = {},
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
    var showDatePicker by rememberSaveable { mutableStateOf(false) }

    if (showDatePicker) {
        RoastDatePickerDialog(
            initial = uiState.roastDate,
            onConfirm = { date ->
                onRoastDateChange(date)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false },
        )
    }

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
                SaveBrewBar(uiState = uiState, onSaveClick = onSaveClick)
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
                        MethodSection(selected = uiState.brewMethod, onSelect = onBrewMethodChange)
                    }
                }

                item {
                    RatingCard(rating = uiState.rating, onRatingChange = onRatingChange)
                }

                item {
                    CoffeeSection(
                        uiState = uiState,
                        onCoffeeBagSelected = onCoffeeBagSelected,
                        onRoasterNameChange = onRoasterNameChange,
                        onBeanNameChange = onBeanNameChange,
                        onOriginChange = onOriginChange,
                        onRoastDateClick = { showDatePicker = true },
                    )
                }

                item {
                    GrindSection(
                        grinder = uiState.grinder,
                        grindSetting = uiState.grindSetting,
                        onGrinderChange = onGrinderChange,
                        onGrindSettingChange = onGrindSettingChange,
                    )
                }

                item {
                    OutlinedTextField(
                        value = uiState.description,
                        onValueChange = onDescriptionChange,
                        label = { Text(text = stringResource(R.string.brew_description)) },
                        placeholder = { Text(text = stringResource(R.string.brew_description_hint)) },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        shape = FieldShape,
                        minLines = 4,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        KawLoadingOverlay(visible = uiState.isLoading)
    }
}

private val FieldShape = RoundedCornerShape(12.dp)

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
@Composable
private fun MethodSection(selected: BrewMethod, onSelect: (BrewMethod) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        SectionTitle(text = stringResource(R.string.section_method))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BrewMethod.entries.forEach { method ->
                FilterChip(
                    selected = selected == method,
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
    onRoastDateClick: () -> Unit,
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
        RoastDateField(date = uiState.roastDate, onClick = onRoastDateClick)
    }
}

@Composable
private fun GrindSection(
    grinder: Grinder,
    grindSetting: String,
    onGrinderChange: (Grinder) -> Unit,
    onGrindSettingChange: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SectionTitle(text = stringResource(R.string.section_grind))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Grinder.entries.forEach { entry ->
                FilterChip(
                    selected = grinder == entry,
                    onClick = { onGrinderChange(entry) },
                    label = { Text(entry.displayName) },
                )
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

/** Read-only field that opens the date picker; future dates are not selectable. */
@Composable
private fun RoastDateField(date: LocalDate?, onClick: () -> Unit) {
    val locale = ConfigurationCompat.getLocales(LocalConfiguration.current)[0] ?: Locale.getDefault()
    val formatter = remember(locale) { DateTimeFormatter.ofPattern("d MMMM yyyy", locale) }

    Box {
        OutlinedTextField(
            value = date?.format(formatter).orEmpty(),
            onValueChange = {},
            readOnly = true,
            label = { Text(text = stringResource(R.string.roast_date_label)) },
            trailingIcon = { Icon(Icons.Outlined.CalendarMonth, contentDescription = null) },
            shape = FieldShape,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
        )
        // The text field would swallow taps for its own cursor, so a transparent layer opens the picker.
        Box(
            modifier = Modifier
                .matchParentSize()
                .padding(top = 8.dp)
                .clip(FieldShape)
                .clickable(onClickLabel = stringResource(R.string.choose_date), onClick = onClick),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RoastDatePickerDialog(
    initial: LocalDate?,
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val today = remember { LocalDate.now() }
    val todayMillis = remember(today) { today.toUtcMillis() }
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initial?.toUtcMillis(),
        selectableDates = remember(todayMillis) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = utcTimeMillis <= todayMillis
                override fun isSelectableYear(year: Int) = year <= today.year
            }
        },
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = { state.selectedDateMillis?.let { onConfirm(it.toUtcLocalDate()) } },
                enabled = state.selectedDateMillis != null,
            ) {
                Text(text = stringResource(R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    ) {
        DatePicker(state = state)
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
        shape = FieldShape,
        singleLine = true,
        modifier = modifier,
    )
}

private fun LocalDate.toUtcMillis(): Long = atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

private fun Long.toUtcLocalDate(): LocalDate = Instant.ofEpochMilli(this).atZone(ZoneOffset.UTC).toLocalDate()