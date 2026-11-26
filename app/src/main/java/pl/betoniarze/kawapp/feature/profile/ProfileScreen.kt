package pl.betoniarze.kawapp.feature.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import java.time.LocalDate
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.model.AppLanguage
import pl.betoniarze.kawapp.core.model.CoffeeBag
import pl.betoniarze.kawapp.core.model.Grinder
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.ThemeMode
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawCard
import pl.betoniarze.kawapp.core.ui.components.KawLoadingOverlay
import pl.betoniarze.kawapp.core.ui.theme.KawTheme
import pl.betoniarze.kawapp.feature.chime_generator.ChimeTone
import pl.betoniarze.kawapp.feature.profile.components.AddCoffeeBagSheet
import pl.betoniarze.kawapp.feature.profile.components.TextInputDialog
import pl.betoniarze.kawapp.ui.components.rememberRoastDateFormatter
import pl.betoniarze.kawapp.ui.photos.PhotoCallbacks
import pl.betoniarze.kawapp.ui.photos.PhotoSourceSheet
import pl.betoniarze.kawapp.ui.photos.rememberPhotoPicker

/**
 * Profile tab connected to [ProfileViewModel]: user name, grinders, coffees and the app settings.
 * Deleting a grinder or coffee shows a snackbar with "Undo".
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsStateWithLifecycle()
    val profilePhotoUri by viewModel.profilePhotoUri.collectAsStateWithLifecycle()
    val photoCallbacks = remember(viewModel) {
        PhotoCallbacks(
            newCameraUri = viewModel::newCameraUri,
            onPhotoTaken = viewModel::onProfilePhotoTaken,
            onCameraCancelled = viewModel::onCameraCancelled,
            onPhotosPicked = viewModel::onProfilePhotoPicked,
        )
    }
    val grinders by viewModel.grinders.collectAsStateWithLifecycle()
    val coffeeBags by viewModel.coffeeBags.collectAsStateWithLifecycle()
    val brewMethods by viewModel.brewMethods.collectAsStateWithLifecycle()
    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val selectedThemeMode by viewModel.selectedThemeMode.collectAsStateWithLifecycle()
    val selectedChimeTone by viewModel.selectedChimeTone.collectAsStateWithLifecycle()
    val isApplyingSettings by viewModel.isApplyingSettings.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val undoLabel = stringResource(R.string.undo)
    val grinderDeletedText = stringResource(R.string.grinder_deleted)
    val coffeeDeletedText = stringResource(R.string.coffee_deleted)
    val brewMethodDeletedText = stringResource(R.string.brewMethod_deleted)

    // Shows "<item> deleted · Undo" and runs [restore] if the user taps Undo.
    fun showUndo(message: String, restore: () -> Unit) {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = undoLabel,
                duration = SnackbarDuration.Short,
            )
            if (result == SnackbarResult.ActionPerformed) restore()
        }
    }

    ProfileScreenContent(
        userName = userName,
        profilePhotoUri = profilePhotoUri,
        photoCallbacks = photoCallbacks,
        onRemoveProfilePhoto = viewModel::removeProfilePhoto,
        brewMethods = brewMethods,
        grinders = grinders,
        coffeeBags = coffeeBags,
        selectedLanguage = selectedLanguage,
        selectedThemeMode = selectedThemeMode,
        selectedChimeTone = selectedChimeTone,
        isApplyingSettings = isApplyingSettings,
        snackbarHostState = snackbarHostState,
        onUserNameChange = viewModel::saveUserName,
        onAddBrewMethod = viewModel::addBrewMethod,
        onDeleteBrewMethod = { brewMethod ->
            viewModel.deleteBrewMethod(brewMethod)
            showUndo(brewMethodDeletedText.format(brewMethod.displayName)) { viewModel.restoreBrewMethod(brewMethod)}
        },
        onAddGrinder = viewModel::addGrinder,
        onDeleteGrinder = { grinder ->
            viewModel.deleteGrinder(grinder)
            showUndo(grinderDeletedText.format(grinder.displayName)) { viewModel.restoreGrinder(grinder) }
        },
        onAddCoffeeBag = viewModel::addCoffeeBag,
        onDeleteCoffeeBag = { bag ->
            viewModel.deleteCoffeeBag(bag)
            showUndo(coffeeDeletedText.format(bag.beanName)) { viewModel.restoreCoffeeBag(bag) }
        },
        onLanguageSelected = viewModel::selectLanguage,
        onThemeModeSelected = viewModel::selectThemeMode,
        onChimeToneSelected = viewModel::selectChimeTone,
        modifier = modifier
    )
}

/**
 * Stateless Profile UI: a header with the user's photo (or monogram) and name, then the brew
 * methods, grinders, coffees and settings. The avatar ring is the one honey element on this screen.
 *
 * @param grinders Null while loading (nothing is shown instead of a flashing empty state).
 * @param isApplyingSettings True while a language/theme change is being applied; shows the loading
 * overlay.
 */
@Composable
fun ProfileScreenContent(
    userName: String,
    brewMethods: List<BrewMethod>?,
    grinders: List<Grinder>?,
    coffeeBags: List<CoffeeBag>?,
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    modifier: Modifier = Modifier,
    selectedThemeMode: ThemeMode = ThemeMode.SYSTEM,
    selectedChimeTone: ChimeTone = ChimeTone.FIRST,
    isApplyingSettings: Boolean = false,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    profilePhotoUri: String = "",
    photoCallbacks: PhotoCallbacks = PhotoCallbacks.None,
    onRemoveProfilePhoto: () -> Unit = {},
    onUserNameChange: (String) -> Unit = {},
    onAddBrewMethod: (String) -> Unit = {},
    onDeleteBrewMethod: (BrewMethod) -> Unit = {},
    onAddGrinder: (String) -> Unit = {},
    onDeleteGrinder: (Grinder) -> Unit = {},
    onAddCoffeeBag: (String, String, String, LocalDate) -> Unit = { _, _, _, _ -> },
    onDeleteCoffeeBag: (CoffeeBag) -> Unit = {},
    onThemeModeSelected: (ThemeMode) -> Unit = {},
    onChimeToneSelected: (ChimeTone) -> Unit = {},
) {
    var showNameDialog by rememberSaveable { mutableStateOf(false) }
    var showGrinderDialog by rememberSaveable { mutableStateOf(false) }
    var showCoffeeSheet by rememberSaveable { mutableStateOf(false) }
    var showBrewMethodDialog by rememberSaveable { mutableStateOf(false) }
    var showPhotoSheet by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val noCameraText = stringResource(R.string.no_camera_app)
    val photoPicker = rememberPhotoPicker(
        maxItems = 1,
        callbacks = photoCallbacks,
        onNoCameraApp = { scope.launch { snackbarHostState.showSnackbar(noCameraText) } },
    )

    if (showPhotoSheet) {
        PhotoSourceSheet(
            title = stringResource(R.string.profile_photo_title),
            onCamera = photoPicker::openCamera,
            onGallery = photoPicker::openGallery,
            onDismiss = { showPhotoSheet = false },
            onRemove = if (profilePhotoUri.isNotEmpty()) onRemoveProfilePhoto else null,
        )
    }

    if (showNameDialog) {
        TextInputDialog(
            title = stringResource(R.string.user_name_dialog_title),
            label = stringResource(R.string.user_name_label),
            confirmText = stringResource(R.string.save),
            initialValue = userName,
            allowBlank = true,
            onConfirm = {
                onUserNameChange(it)
                showNameDialog = false
            },
            onDismiss = { showNameDialog = false },
        )
    }
    if (showBrewMethodDialog) {
        TextInputDialog(
            title = stringResource(R.string.add_brewMethod_title),
            label = stringResource(R.string.brewMethod_name),
            placeholder = stringResource(R.string.brewMethod_name_hint),
            confirmText = stringResource(R.string.add),
            onConfirm = {
                onAddBrewMethod(it)
                showBrewMethodDialog = false
            },
            onDismiss = { showBrewMethodDialog = false },
        )
    }
    if (showGrinderDialog) {
        TextInputDialog(
            title = stringResource(R.string.add_grinder_title),
            label = stringResource(R.string.grinder_name),
            placeholder = stringResource(R.string.grinder_name_hint),
            confirmText = stringResource(R.string.add),
            onConfirm = {
                onAddGrinder(it)
                showGrinderDialog = false
            },
            onDismiss = { showGrinderDialog = false },
        )
    }
    if (showCoffeeSheet) {
        AddCoffeeBagSheet(
            onConfirm = { roaster, bean, origin, roastDate ->
                onAddCoffeeBag(roaster, bean, origin, roastDate)
                showCoffeeSheet = false
            },
            onDismiss = { showCoffeeSheet = false },
        )
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileHeader(
                userName = userName,
                photoUri = profilePhotoUri,
                onPhotoClick = { showPhotoSheet = true },
                onEditName = { showNameDialog = true },
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                item {
                    SectionHeader(
                        title = stringResource(R.string.profile_brewMethods),
                        onAdd = { showBrewMethodDialog = true },
                    )
                }
                if (brewMethods != null) item {
                    ItemListCard(
                        items = brewMethods,
                        emptyText = stringResource(R.string.brewMethods_empty),
                        key = { it.id },
                    ) { brewMethod ->
                        ItemRow(
                            title = brewMethod.displayName,
                            deleteDescription = stringResource(R.string.delete_item, brewMethod.displayName),
                            onDelete = if (brewMethod.isBuiltIn) null else { { onDeleteBrewMethod(brewMethod) } },
                        )
                    }
                }
                item {
                    SectionHeader(
                        title = stringResource(R.string.profile_grinders),
                        onAdd = { showGrinderDialog = true },
                    )
                }
                if (grinders != null) item {
                    ItemListCard(
                        items = grinders,
                        emptyText = stringResource(R.string.grinders_empty),
                        key = { it.id },
                    ) { grinder ->
                        ItemRow(
                            title = grinder.displayName,
                            deleteDescription = stringResource(R.string.delete_item, grinder.displayName),
                            onDelete = { onDeleteGrinder(grinder) },
                        )
                    }
                }

                item {
                    SectionHeader(
                        title = stringResource(R.string.profile_coffees),
                        onAdd = { showCoffeeSheet = true },
                        modifier = Modifier.padding(top = 12.dp),
                    )
                }
                if (coffeeBags != null) item {
                    val formatter = rememberRoastDateFormatter()
                    ItemListCard(
                        items = coffeeBags,
                        emptyText = stringResource(R.string.coffees_empty),
                        key = { it.id },
                    ) { bag ->
                        ItemRow(
                            title = "${bag.origin} ${bag.beanName}".trim(),
                            subtitle = stringResource(
                                R.string.brew_log_subtitle,
                                bag.roasterName,
                                stringResource(R.string.roast_date, bag.roastDate.format(formatter)),
                            ),
                            deleteDescription = stringResource(R.string.delete_item, bag.beanName),
                            onDelete = { onDeleteCoffeeBag(bag) },
                        )
                    }
                }

                item {
                    Text(
                        text = stringResource(R.string.profile_settings),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .padding(top = 12.dp, start = 2.dp)
                            .semantics { heading() },
                    )
                }
                item {
                    SettingCard(
                        title = stringResource(R.string.profile_language_setting),
                        options = AppLanguage.entries,
                        selected = selectedLanguage,
                        onSelected = onLanguageSelected,
                        label = { language ->
                            when (language) {
                                AppLanguage.POLISH -> stringResource(R.string.language_polish)
                                AppLanguage.ENGLISH -> stringResource(R.string.language_english)
                            }
                        },
                    )
                }
                item {
                    SettingCard(
                        title = stringResource(R.string.profile_theme_setting),
                        options = ThemeMode.entries,
                        selected = selectedThemeMode,
                        onSelected = onThemeModeSelected,
                        label = { mode ->
                            when (mode) {
                                ThemeMode.SYSTEM -> stringResource(R.string.theme_system)
                                ThemeMode.LIGHT -> stringResource(R.string.theme_light)
                                ThemeMode.DARK -> stringResource(R.string.theme_dark)
                            }
                        },
                    )
                }
                item {
                    SettingCard(
                        title = stringResource(R.string.profile_chime_setting),
                        options = ChimeTone.entries,
                        selected = selectedChimeTone,
                        onSelected = onChimeToneSelected,
                        label = { tone ->
                            when (tone) {
                                ChimeTone.FIRST -> stringResource(R.string.chime_high)
                                ChimeTone.SECOND -> stringResource(R.string.chime_low)
                                ChimeTone.BOTH -> stringResource(R.string.chime_both)
                            }
                        },
                    )
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp),
        )

        // Covers the Activity being recreated after a language/theme change.
        // Already visible when the screen comes back, so don't fade it in again.
        KawLoadingOverlay(visible = isApplyingSettings, animateFirstShow = false)
    }
}

/**
 * Header in the Feed / Journal style, drawn behind the status bar: the avatar (tap to change the
 * photo) and the name (tap to edit it).
 */
@Composable
private fun ProfileHeader(
    userName: String,
    photoUri: String,
    onPhotoClick: () -> Unit,
    onEditName: () -> Unit,
) {
    val hasName = userName.isNotBlank()
    val editLabel = stringResource(if (hasName) R.string.profile_edit_name else R.string.profile_add_name)

    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 14.dp, end = 4.dp, top = 14.dp, bottom = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ProfileAvatar(name = userName, photoUri = photoUri, onClick = onPhotoClick)
            Spacer(Modifier.width(8.dp))
            Surface(
                onClick = onEditName,
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = if (hasName) userName else stringResource(R.string.profile_add_name),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
                        color = if (hasName) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = editLabel,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
        }
    }
}

/**
 * Profile photo in a honey ring (the one honey element on this screen), or the first letter of
 * [name] when there is no photo. A small camera badge shows that it can be tapped.
 */
@Composable
private fun ProfileAvatar(
    name: String,
    photoUri: String,
    onClick: () -> Unit,
) {
    val description = stringResource(R.string.profile_photo_change)

    Box(
        modifier = Modifier
            .size(62.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 34.dp),
                role = Role.Button,
                onClick = onClick,
            )
            .semantics { contentDescription = description },
    ) {
        Surface(
            shape = CircleShape,
            color = KawTheme.colors.tagContainer,
            contentColor = KawTheme.colors.onTag,
            border = BorderStroke(1.5.dp, KawTheme.colors.tagBorder),
            modifier = Modifier
                .size(58.dp)
                .align(Alignment.Center),
        ) {
            Box(contentAlignment = Alignment.Center) {
                val initial = name.trim().firstOrNull()?.uppercase()
                when {
                    photoUri.isNotEmpty() -> AsyncImage(
                        model = photoUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape),
                    )
                    initial != null -> Text(
                        text = initial,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Medium,
                    )
                    else -> Icon(Icons.Outlined.LocalCafe, contentDescription = null, modifier = Modifier.size(26.dp))
                }
            }
        }
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            border = BorderStroke(2.dp, MaterialTheme.colorScheme.surfaceContainer),
            modifier = Modifier
                .size(24.dp)
                .align(Alignment.BottomEnd),
        ) {
            Icon(
                imageVector = Icons.Outlined.PhotoCamera,
                contentDescription = null,
                modifier = Modifier.padding(4.dp),
            )
        }
    }
}

/** Section title with an "Add" text button on the right. */
@Composable
private fun SectionHeader(
    title: String,
    onAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .weight(1f)
                .padding(start = 2.dp)
                .semantics { heading() },
        )
        TextButton(onClick = onAdd) {
            Icon(Icons.Filled.Add, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(text = stringResource(R.string.add))
        }
    }
}

/**
 * One KawCard holding [items] as rows divided by hairlines, or [emptyText] when there are none.
 * Grouping the rows in one card keeps a short list calm compared to a card per item.
 */
@Composable
private fun <T> ItemListCard(
    items: List<T>,
    emptyText: String,
    key: (T) -> Any,
    row: @Composable (T) -> Unit,
) {
    if (items.isEmpty()) {
        KawCard {
            Text(
                text = emptyText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        return
    }
    KawCard(contentPadding = PaddingValues(vertical = 2.dp)) {
        items.forEachIndexed { index, item ->
            androidx.compose.runtime.key(key(item)) {
                if (index > 0) {
                    HorizontalDivider(
                        color = KawTheme.colors.cardBorder,
                        modifier = Modifier.padding(start = 14.dp),
                    )
                }
                row(item)
            }
        }
    }
}

/** A list row: title, optional subtitle and a delete button. */
@Composable
private fun ItemRow(
    title: String,
    deleteDescription: String,
    onDelete: (() -> Unit)?,
    subtitle: String? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .padding(
                start = 14.dp,
                end = if (onDelete != null) 2.dp else 14.dp,
                top = 2.dp,
                bottom = 2.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (onDelete != null) {
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = deleteDescription,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

/** A settings card: [title] and a segmented single choice between [options]. */
@Composable
private fun <T> SettingCard(
    title: String,
    options: List<T>,
    selected: T,
    onSelected: (T) -> Unit,
    label: @Composable (T) -> String,
    modifier: Modifier = Modifier,
) {
    KawCard(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
            options.forEachIndexed { index, option ->
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { if (option != selected) onSelected(option) },
                    selected = option == selected,
                ) {
                    Text(label(option))
                }
            }
        }
    }
}
