package pl.betoniarze.kawapp.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import pl.betoniarze.kawapp.core.data.repository.BrewLogRepository
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.model.BrewLog
import pl.betoniarze.kawapp.core.model.FeedPost
import pl.betoniarze.kawapp.feature.journal.calculateStreak
import pl.betoniarze.kawapp.feature.profile.UserNameStore
import kotlin.collections.mutableSetOf

/**
 * A feed post together with the current user's Kawos state.
 *
 * @property kavosCount Count shown in the UI, including the current user's Kawos.
 */
data class FeedPostItem(
    val post: FeedPost,
    val kavosGiven: Boolean,
    val kavosCount: Int,
    val brew: BrewLog? = null
)

/** State of the Feed screen. */
sealed interface FeedUiState {
    /** Posts are not loaded yet; the screen shows the loading overlay. */
    data object Loading : FeedUiState
    /**
     * Posts are loaded.
     *
     * @property userName Name shown in the "Hello, <name>!" header.
     * @property streakDays Consecutive days with a brew, shown under the greeting when above 0.
     */
    data class Success(
        val posts: List<FeedPostItem>,
        val userName: String,
        val streakDays: Int,
    ) : FeedUiState
}

/** ViewModel of the Feed: posts, the greeting header and the user's Kawos. */
@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    brewLogRepository: BrewLogRepository,
    userNameStore: UserNameStore
) : ViewModel() {

    // TODO: take it from the profile / login once there is a user model.
    /** Placeholder user name for the greeting header. */

    // TODO: save given Kawos to the repository / Firestore.
    /**
     * Ids of posts the user gave a Kawos. Kept here (not mutated in the UI), so it survives
     * scrolling.
     */
    private val givenKavos = MutableStateFlow<Set<String>>(emptySet())

    /** Posts with Kawos state, the user name and the streak. Starts as [FeedUiState.Loading]. */
    val uiState: StateFlow<FeedUiState> = combine(
        feedRepository.getFeedPosts(),
        givenKavos,
        brewLogRepository.getAllBrewLogs(),
        userNameStore.userName
    ) { posts, given, brewLogs, userName ->
        val brewsById = brewLogs.associateBy { it.id }
        FeedUiState.Success(
            userName = userName,
            streakDays = calculateStreak(brewLogs.mapTo(mutableSetOf()) { it.date }, LocalDate.now()),
            posts = posts.map { post ->
                val isGiven = post.id in given
                FeedPostItem(
                    post = post,
                    kavosGiven = isGiven,
                    kavosCount = post.kavosCount + if (isGiven) 1 else 0,
                    brew = post.brewLogId?.let(brewsById::get),
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedUiState.Loading
    )

    /** Gives or takes back the user's Kawos on the post with [postId]. */
    fun onKavosClick(postId: String) {
        givenKavos.update { if (postId in it) it - postId else it + postId }
    }
}
