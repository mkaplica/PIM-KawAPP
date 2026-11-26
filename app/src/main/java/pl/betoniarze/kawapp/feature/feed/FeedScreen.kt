package pl.betoniarze.kawapp.feature.feed

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawListDefaults
import pl.betoniarze.kawapp.core.ui.components.KawLoadingOverlay
import pl.betoniarze.kawapp.core.ui.components.KawStreakTag
import pl.betoniarze.kawapp.feature.feed.components.FeedPostCard

/**
 * Feed tab: community posts under a greeting header. Connects [FeedScreenContent] to
 * [FeedViewModel].
 */
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

/** Stateless Feed UI for [uiState]: header and post list, or the loading overlay while loading. */
@Composable
fun FeedScreenContent(
    uiState: FeedUiState,
    modifier: Modifier = Modifier,
    onKavosClick: (String) -> Unit = {},
    onCommentsClick: (String) -> Unit = {},
    onShareClick: (String) -> Unit = {},
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (uiState is FeedUiState.Success) Column(modifier = Modifier.fillMaxSize()) {
            FeedHeader(userName = uiState.userName, streakDays = uiState.streakDays)

            if (uiState.posts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center,
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
                            brew = item.brew
                        )
                    }
                }
            }
        }

        KawLoadingOverlay(visible = uiState is FeedUiState.Loading)
    }
}

/** Feed header in the Journal style: greeting + coffee streak, drawn behind the status bar. */
@Composable
private fun FeedHeader(
    userName: String,
    streakDays: Int,
    modifier: Modifier = Modifier,
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainer,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = if (userName.isBlank()) {
                    stringResource(R.string.feed_greeting_no_name)
                } else {
                    stringResource(R.string.feed_greeting, userName)
                },
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Normal),
            )
            if (streakDays > 0) {
                KawStreakTag(streakDays = streakDays, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}
