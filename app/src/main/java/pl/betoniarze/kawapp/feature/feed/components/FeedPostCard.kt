package pl.betoniarze.kawapp.feature.feed.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import pl.betoniarze.kawapp.core.model.FeedPost
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawTag
import pl.betoniarze.kawapp.core.ui.components.KawTagColors
import pl.betoniarze.kawapp.core.ui.components.KawTagDefaults
import pl.betoniarze.kawapp.core.ui.theme.KawTheme

/**
 * Post w feedzie. Miodowy akcent na tym ekranie mają TYLKO przyznane Kawosy.
 * Modyfikator trafia wyłącznie do karty (wcześniej był też przekazywany do LazyRow i rzędu akcji).
 */
@Composable
fun FeedPostCard(
    post: FeedPost,
    kavosGiven: Boolean,
    kavosCount: Int,
    onKavosClick: () -> Unit,
    modifier: Modifier = Modifier,
    onCommentsClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = KawTheme.colors.card,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(1.dp, KawTheme.colors.cardBorder),
    ) {
        Column {
            // Nagłówek
            Row(
                modifier = Modifier.padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                AsyncImage(
                    model = post.authorAvatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.outlineVariant),
                )
                Column {
                    Text(text = post.authorName, style = MaterialTheme.typography.titleMedium)
                    // TODO: czas publikacji + metoda parzenia (brak pól w FeedPost).
                }
            }

            // TODO: tytuł i statystyki parzenia (KawStatRow), gdy FeedPost dostanie dane o naparze.

            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 14.dp, end = 14.dp, bottom = 10.dp),
            )

            if (post.imageUrls.isNotEmpty()) {
                PostImages(urls = post.imageUrls)
            }

            // Akcje wyrównane do lewej, udostępnianie po prawej.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                KavosButton(given = kavosGiven, count = kavosCount, onClick = onKavosClick)

                TextButton(
                    onClick = onCommentsClick,
                    contentPadding = PaddingValues(horizontal = 10.dp),
                ) {
                    Icon(
                        Icons.Outlined.ModeComment,
                        contentDescription = stringResource(R.string.comments),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Spacer(Modifier.size(6.dp))
                    Text(
                        text = post.comments.count().toString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                Spacer(Modifier.weight(1f))

                IconButton(onClick = onShareClick) {
                    Icon(
                        Icons.Outlined.Share,
                        contentDescription = stringResource(R.string.share),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun KavosButton(given: Boolean, count: Int, onClick: () -> Unit) {
    // Ten sam komponent KawTag: przyznane = miodowy outlined, nieprzyznane = bez tła i obwódki.
    val colors = if (given) {
        KawTagDefaults.accent()
    } else {
        KawTagColors(
            container = Color.Transparent,
            content = MaterialTheme.colorScheme.onSurfaceVariant,
            border = Color.Transparent,
        )
    }
    KawTag(
        text = pluralStringResource(R.plurals.kavos_count, count, count),
        colors = colors,
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
        leading = {
            Icon(
                imageVector = if (given) Icons.Filled.LocalCafe else Icons.Outlined.LocalCafe,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
            )
        },
    )
}

@Composable
private fun PostImages(urls: List<String>) {
    val pagerState = rememberPagerState(pageCount = { urls.size })
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(16f / 10f)
            .background(MaterialTheme.colorScheme.surfaceContainerHighest),
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            AsyncImage(
                model = urls[page],
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize(),
            )
        }
        if (urls.size > 1) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                repeat(urls.size) { index ->
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(
                                if (index == pagerState.currentPage) Color.White
                                else Color.White.copy(alpha = 0.5f)
                            )
                    )
                }
            }
        }
    }
}
