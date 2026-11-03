package pl.betoniarze.kawapp.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.betoniarze.kawapp.core.model.FeedPost
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.feature.feed.components.FeedPostCard

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FeedScreenContent(
        uiState = uiState,
        modifier = modifier
    )
}

@Composable
fun FeedScreenContent(
    uiState: FeedUiState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is FeedUiState.Loading -> CircularProgressIndicator()
            is FeedUiState.Success -> {
                if (uiState.posts.isEmpty()) {
                    Text(
                        text = stringResource(R.string.feed_empty_text),
                        style = MaterialTheme.typography.headlineLarge,
                    )
                }
                else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        items(
                            items = uiState.posts,
                            key = { post -> post.id }
                        ) { post ->
                            FeedPostCard(
                                post = post
                            )
                        }
                    }
                }
            }
        }
    }
}
