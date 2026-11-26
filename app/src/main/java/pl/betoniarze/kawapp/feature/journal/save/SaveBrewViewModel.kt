package pl.betoniarze.kawapp.feature.journal.save

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import pl.betoniarze.kawapp.core.data.repository.BrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.BrewMethodRepository
import pl.betoniarze.kawapp.core.data.repository.CoffeeBagRepository
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.data.repository.GrinderRepository
import pl.betoniarze.kawapp.core.data.repository.RecipeRepository
import pl.betoniarze.kawapp.core.model.BrewLog
import pl.betoniarze.kawapp.core.model.BrewMethod
import pl.betoniarze.kawapp.core.model.CoffeeBag
import pl.betoniarze.kawapp.core.model.CoffeeRecipe
import pl.betoniarze.kawapp.core.model.FeedPost
import pl.betoniarze.kawapp.core.model.Grinder
import pl.betoniarze.kawapp.feature.profile.UserNameStore
import pl.betoniarze.kawapp.ui.components.MAX_RATING

/** How many coffees are offered as quick picks. */
private const val RECENT_BAGS_LIMIT = 8

/**
 * State of the "save brew" form.
 *
 * @property recipe Recipe that was just brewed; null when the form was opened from the Journal.
 * @property brewMethod Used only when [recipe] is null (otherwise the recipe's method wins).
 * @property brewMethods Built-in and Profile methods, offered when there is no [recipe].
 * @property rating 1–10, 0 until the user picks one.
 * @property grinders The user's grinders from the Profile; [grinder] is one of them or null.
 * @property recentBags Coffees from the Profile, then from earlier brews, for one-tap filling.
 * @property isSaved Set after saving; the screen then navigates away.
 */
data class SaveBrewUiState(
    val recipe: CoffeeRecipe? = null,
    val brewMethod: BrewMethod = BrewMethod.V60,
    val brewMethods: List<BrewMethod> = BrewMethod.BUILT_IN,
    val rating: Int = 0,
    val roasterName: String = "",
    val beanName: String = "",
    val origin: String = "",
    val roastDate: LocalDate? = null,
    val grinders: List<Grinder> = emptyList(),
    val grinder: Grinder? = null,
    val grindSetting: String = "",
    val description: String = "",
    val recentBags: List<CoffeeBag> = emptyList(),
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val isSaved: Boolean = false,
) {
    val hasRating: Boolean get() = rating in 1..MAX_RATING

    /** Roaster, beans and roast date are required; origin is optional. */
    val hasCoffee: Boolean
        get() = roasterName.isNotBlank() && beanName.isNotBlank() && roastDate != null

    val canSave: Boolean get() = hasRating && hasCoffee && !isSaving && !isLoading

    /** Whether [bag] is the coffee currently filled in (highlights its quick-pick chip). */
    fun isSelected(bag: CoffeeBag): Boolean =
        bag.roasterName == roasterName && bag.beanName == beanName && bag.roastDate == roastDate
}

/**
 * ViewModel of the "save brew" form. Opened with an optional `recipeId` navigation argument.
 *
 * Saving creates one [BrewLog] (Journal + calendar) and one [FeedPost] that points at it through
 * [FeedPost.brewLogId], so rating, coffee and method live in a single place.
 */
@HiltViewModel
class SaveBrewViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recipeRepository: RecipeRepository,
    private val brewLogRepository: BrewLogRepository,
    private val feedRepository: FeedRepository,
    private val grinderRepository: GrinderRepository,
    private val coffeeBagRepository: CoffeeBagRepository,
    private val userNameStore: UserNameStore,
    private val brewMethodRepository: BrewMethodRepository,
) : ViewModel() {

    private val recipeId: String? = savedStateHandle.get<String>("recipeId")?.takeIf { it.isNotBlank() }

    private val _uiState = MutableStateFlow(SaveBrewUiState())
    val uiState: StateFlow<SaveBrewUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    /**
     * Loads the recipe and pre-fills the coffee and grinder from the latest brew (of this recipe if
     * there is one), since people usually brew the same bag several days in a row.
     */
    private fun load() {
        viewModelScope.launch {
            val recipe = recipeId?.let { recipeRepository.getRecipeById(it).first() }
            val logs = brewLogRepository.getAllBrewLogs().first()
            val grinders = grinderRepository.getGrinders().first()
            val savedBags = coffeeBagRepository.getCoffeeBags().first()
            val brewMethods = brewMethodRepository.getBrewMethods().first()
            val previous = logs.firstOrNull { recipeId != null && it.recipeId == recipeId }
                ?: logs.firstOrNull()
            // Last used method if it is still available, otherwise the first one (Hario V60).
            val defaultMethod = brewMethods.firstOrNull { it.id == previous?.brewMethod?.id }
                ?: brewMethods.firstOrNull()

            _uiState.update { state ->
                state.copy(
                    recipe = recipe,
                    brewMethods = brewMethods,
                    brewMethod = recipe?.brewMethod ?: defaultMethod ?: state.brewMethod,
                    roasterName = previous?.coffeeBag?.roasterName.orEmpty(),
                    beanName = previous?.coffeeBag?.beanName.orEmpty(),
                    origin = previous?.coffeeBag?.origin.orEmpty(),
                    roastDate = previous?.coffeeBag?.roastDate,
                    grinders = grinders,
                    // The last used grinder if it is still in the Profile, otherwise the first one.
                    grinder = grinders.firstOrNull { it.id == previous?.grinder?.id }
                        ?: grinders.firstOrNull(),
                    grindSetting = previous?.grindSetting.orEmpty(),
                    recentBags = (savedBags + logs.map { it.coffeeBag })
                        .distinctBy { Triple(it.roasterName, it.beanName, it.roastDate) }
                        .take(RECENT_BAGS_LIMIT),
                    isLoading = false,
                )
            }
        }
    }

    fun onRatingChange(rating: Int) {
        _uiState.update { it.copy(rating = rating.coerceIn(1, MAX_RATING)) }
    }

    fun onBrewMethodChange(method: BrewMethod) {
        _uiState.update { it.copy(brewMethod = method) }
    }

    /** Fills all coffee fields from a recently brewed bag. */
    fun onCoffeeBagSelected(bag: CoffeeBag) {
        _uiState.update {
            it.copy(
                roasterName = bag.roasterName,
                beanName = bag.beanName,
                origin = bag.origin,
                roastDate = bag.roastDate,
            )
        }
    }

    fun onRoasterNameChange(value: String) = _uiState.update { it.copy(roasterName = value) }
    fun onBeanNameChange(value: String) = _uiState.update { it.copy(beanName = value) }
    fun onOriginChange(value: String) = _uiState.update { it.copy(origin = value) }
    fun onRoastDateChange(value: LocalDate) = _uiState.update { it.copy(roastDate = value) }
    fun onGrinderChange(value: Grinder) = _uiState.update { it.copy(grinder = value) }
    fun onGrindSettingChange(value: String) = _uiState.update { it.copy(grindSetting = value) }
    fun onDescriptionChange(value: String) = _uiState.update { it.copy(description = value) }

    /** Saves the brew to the Journal and publishes it on the Feed. Ignored while invalid or saving. */
    fun saveBrew() {
        val state = _uiState.value
        val roastDate = state.roastDate
        if (!state.canSave || roastDate == null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val now = LocalDateTime.now()
            val description = state.description.trim()
            val brewLog = BrewLog(
                id = UUID.randomUUID().toString(),
                brewMethod = state.recipe?.brewMethod ?: state.brewMethod,
                date = now.toLocalDate(),
                time = now.format(TIME_FORMAT),
                coffeeBag = CoffeeBag(
                    roasterName = state.roasterName.trim(),
                    beanName = state.beanName.trim(),
                    origin = state.origin.trim(),
                    roastDate = roastDate,
                ),
                recipeId = state.recipe?.id.orEmpty(),
                recipeTitle = state.recipe?.title.orEmpty(),
                grinder = state.grinder,
                grindSetting = state.grindSetting.trim(),
                tastingNotes = description,
                rating = state.rating,
            )
            brewLogRepository.saveBrewLog(brewLog)

            feedRepository.addFeedPost(
                FeedPost(
                    id = UUID.randomUUID().toString(),
                    authorName = userNameStore.authorName,
                    authorAvatarUrl = "",
                    content = description,
                    imageUrls = emptyList(),
                    kavosCount = 0,
                    comments = emptyList(),
                    brewLogId = brewLog.id,
                    createdAt = System.currentTimeMillis(),
                )
            )

            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }

    private companion object {
        val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    }
}
