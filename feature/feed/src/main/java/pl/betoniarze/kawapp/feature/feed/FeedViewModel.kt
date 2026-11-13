package pl.betoniarze.kawapp.feature.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.model.FeedPost

sealed interface FeedUiState {
    data object Loading : FeedUiState
    data class Success(val posts: List<FeedPost>) : FeedUiState
}

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository
) : ViewModel() {

    val uiState: StateFlow<FeedUiState> = feedRepository.getFeedPosts()
        .map { posts -> FeedUiState.Success(posts) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FeedUiState.Loading
        )
}
