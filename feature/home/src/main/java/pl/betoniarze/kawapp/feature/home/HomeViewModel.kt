package pl.betoniarze.kawapp.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import pl.betoniarze.kawapp.core.data.repository.FakeFeedRepository
import pl.betoniarze.kawapp.core.data.repository.FeedRepository
import pl.betoniarze.kawapp.core.model.FeedPost

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val posts: List<FeedPost>) : HomeUiState
}

class HomeViewModel(
    private val feedRepository: FeedRepository = FakeFeedRepository()
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = feedRepository.getFeedPosts()
        .map { posts -> HomeUiState.Success(posts) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiState.Loading
        )
}
