package pl.betoniarze.kawapp.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawListDefaults
import pl.betoniarze.kawapp.feature.feed.components.FeedPostCard

@Composable
fun FeedScreen(
    modifier: Modifier = Modifier,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    FeedScreenContent(
        uiState = uiState,
        onKavosClick = viewModel::onKavosClick,
        modifier = modifier
    )
}

@Composable
fun FeedScreenContent(
    uiState: FeedUiState,
    modifier: Modifier = Modifier,
    onKavosClick: (String) -> Unit = {},
    onCommentsClick: (String) -> Unit = {},
    onShareClick: (String) -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is FeedUiState.Loading -> CircularProgressIndicator()
            is FeedUiState.Success -> {
                if (uiState.posts.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.feed_empty_text),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = KawListDefaults.ContentPadding,
                        verticalArrangement = Arrangement.spacedBy(KawListDefaults.ItemSpacing),
                    ) {
                        items(items = uiState.posts, key = { it.post.id }) { item ->
                            FeedPostCard(
                                post = item.post,
                                kavosGiven = item.kavosGiven,
                                kavosCount = item.kavosCount,
                                onKavosClick = { onKavosClick(item.post.id) },
                                onCommentsClick = { onCommentsClick(item.post.id) },
                                onShareClick = { onShareClick(item.post.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}
