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
import pl.betoniarze.kawapp.core.model.FeedPost
import pl.betoniarze.kawapp.feature.journal.calculateStreak

data class FeedPostItem(
    val post: FeedPost,
    val kavosGiven: Boolean,
    val kavosCount: Int,
)

sealed interface FeedUiState {
    data object Loading : FeedUiState
    data class Success(
        val posts: List<FeedPostItem>,
        val userName: String,
        val streakDays: Int,
    ) : FeedUiState
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    brewLogRepository: BrewLogRepository,
) : ViewModel() {

    // TODO: pobierać z profilu / logowania, gdy pojawi się model użytkownika.
    private val userName = "Barista"

    // Stan Kawosów trzymany w ViewModelu (wcześniej mutacja post.kavosCount w UI,
    // która ginęła przy przewinięciu). TODO: zapis do repozytorium / Firestore.
    private val givenKavos = MutableStateFlow<Set<String>>(emptySet())

    val uiState: StateFlow<FeedUiState> = combine(
        feedRepository.getFeedPosts(),
        givenKavos,
        brewLogRepository.getDatesWithBrews(),
    ) { posts, given, datesWithBrews ->
        FeedUiState.Success(
            userName = userName,
            streakDays = calculateStreak(datesWithBrews, LocalDate.now()),
            posts = posts.map { post ->
                val isGiven = post.id in given
                FeedPostItem(
                    post = post,
                    kavosGiven = isGiven,
                    kavosCount = post.kavosCount + if (isGiven) 1 else 0,
                )
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = FeedUiState.Loading
    )

    fun onKavosClick(postId: String) {
        givenKavos.update { if (postId in it) it - postId else it + postId }
    }
}
