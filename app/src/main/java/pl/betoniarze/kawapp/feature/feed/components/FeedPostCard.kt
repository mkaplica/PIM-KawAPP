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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.os.ConfigurationCompat
import coil3.compose.AsyncImage
import pl.betoniarze.kawapp.core.model.BrewLog
import pl.betoniarze.kawapp.core.model.FeedPost
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.components.KawTag
import pl.betoniarze.kawapp.core.ui.components.KawTagColors
import pl.betoniarze.kawapp.core.ui.components.KawTagDefaults
import pl.betoniarze.kawapp.core.ui.theme.KawTheme
import pl.betoniarze.kawapp.ui.components.RatingLabel
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.runtime.remember
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


/**
 * A post in the feed: author, text, photos and the Kawos / comments / share actions.
 * On this screen ONLY given Kawos use the honey accent. [modifier] is applied to the card only.
 *
 * @param kavosGiven Whether the current user gave this post a Kawos.
 * @param kavosCount Kawos count including the current user's one.
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
    brew: BrewLog? = null
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = KawTheme.colors.card,
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = BorderStroke(1.dp, KawTheme.colors.cardBorder),
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 8.dp),
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                rememberPostTime(post, brew)?.let { time ->
                    Text(
                        text = time,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                    )
                }
            }

            if (post.content.isNotBlank()) {
                Text(
                    text = post.content,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 14.dp, end = 14.dp, bottom = 10.dp),
                )
            }

            if (post.imageUrls.isNotEmpty()) {
                PostImages(urls = post.imageUrls)
            }

            // Actions aligned to the left, share on the right.
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

/**
 * Kawos button built from the same KawTag: honey outlined when [given], no background and border
 * otherwise.
 */
@Composable
private fun KavosButton(given: Boolean, count: Int, onClick: () -> Unit) {
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

/** Swipeable post photos (16:10) with page dots when there is more than one. */
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

/** Brew shared in the post: method, rating, coffee and roaster / recipe. Neutral colors only. */
@Composable
private fun BrewSummary(brew: BrewLog) {
    Column(
        modifier = Modifier.padding(start = 14.dp, end = 14.dp, bottom = 10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            KawTag(text = brew.brewMethod.displayName, colors = KawTagDefaults.neutral())
            if (brew.rating > 0) RatingLabel(rating = brew.rating)
        }
        Text(
            text = "${brew.coffeeBag.origin} ${brew.coffeeBag.beanName}".trim(),
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = if (brew.recipeTitle.isBlank()) brew.coffeeBag.roasterName
            else stringResource(R.string.brew_log_subtitle, brew.coffeeBag.roasterName, brew.recipeTitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

/**
 * When the post was published: [FeedPost.createdAt], or for older posts without it the time of
 * the shared brew. Null when neither is known (e.g. sample posts), then nothing is shown.
 */
@Composable
private fun rememberPostTime(post: FeedPost, brew: BrewLog?): String? {
    val locale = ConfigurationCompat.getLocales(LocalConfiguration.current)[0] ?: LocalLocale.current.platformLocale
    val todayLabel = stringResource(R.string.today)
    val yesterdayLabel = stringResource(R.string.yesterday)

    return remember(post.createdAt, brew?.date, brew?.time, locale, todayLabel, yesterdayLabel) {
        val dateTime = when {
            post.createdAt > 0 ->
                Instant.ofEpochMilli(post.createdAt).atZone(ZoneId.systemDefault()).toLocalDateTime()
            brew != null ->
                runCatching { brew.date.atTime(LocalTime.parse(brew.time)) }
                    .getOrElse { brew.date.atStartOfDay() }
            else -> return@remember null
        }
        formatPostTime(dateTime, LocalDate.now(), locale, todayLabel, yesterdayLabel)
    }
}

/** "Dziś, 17:59" / "Wczoraj, 08:30" / "24 lis, 08:30" / "24 lis 2025, 08:30". */
private fun formatPostTime(
    dateTime: LocalDateTime,
    today: LocalDate,
    locale: Locale,
    todayLabel: String,
    yesterdayLabel: String,
): String {
    val time = dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale))
    val date = dateTime.toLocalDate()
    val day = when (date) {
        today -> todayLabel
        today.minusDays(1) -> yesterdayLabel
        else -> {
            val pattern = if (date.year == today.year) "d MMM" else "d MMM yyyy"
            date.format(DateTimeFormatter.ofPattern(pattern, locale))
        }
    }
    return "$day, $time"
}
