package pl.betoniarze.kawapp.feature.feed.components


import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.outlined.LocalCafe
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import pl.betoniarze.kawapp.core.model.FeedPost


@Composable
fun FeedPostCard(
    modifier: Modifier = Modifier,
    post: FeedPost,
) {
    ElevatedCard(
        shape = shape,
        modifier = modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AsyncImage(
                    model = post.authorAvatarUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                )
                Text(
                    text = post.authorName,
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp),
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp),
            )

            val listState = rememberLazyListState()
            val snapLayoutInfoProvider = remember(listState) {
                SnapLayoutInfoProvider(listState, SnapPosition.Start)
            }
            val decay = rememberSplineBasedDecay<Float>()
            val flingBehavior = remember(snapLayoutInfoProvider, decay) {
                snapFlingBehavior(
                    snapLayoutInfoProvider = snapLayoutInfoProvider,
                    decayAnimationSpec = decay,
                    snapAnimationSpec = spring(stiffness = Spring.StiffnessMedium)
                )
            }

            LazyRow(
                state = listState,
                flingBehavior = flingBehavior,
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(post.imageUrls) { url ->
                    AsyncImage(
                        model = url,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .width(300.dp)
                            .height(200.dp)
                            .clip(shape),
                    )
                }
            }

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                var defaultColor = MaterialTheme.colorScheme.onSurfaceVariant

                var isLiked by remember { mutableStateOf(false) }
                var likeColor = if (isLiked) MaterialTheme.colorScheme.primary else defaultColor
                TextButton(
                    onClick = {
                        isLiked = !isLiked

                        if (isLiked) post.kavosCount += 1 else post .kavosCount -=1
                    }


                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = if (isLiked) Icons.Filled.LocalCafe else Icons.Outlined.LocalCafe,
                            tint = likeColor,
                            contentDescription = null,
                        )
                        Text(
                            text = post.kavosCount.toString(),
                            color = likeColor,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }

                TextButton(
                    onClick = {}
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ModeComment,
                            contentDescription = null,
                            tint = defaultColor
                        )
                        Text(
                            text = post.comments.count().toString(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = defaultColor,
                        )
                    }
                }

                TextButton(
                    onClick = {},
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = null,
                            tint = defaultColor

                        )
                    }
                }
            }
        }
    }
}