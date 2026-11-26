package pl.betoniarze.kawapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.progressBarRangeInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.theme.KawTheme

/** Highest possible brew rating. */
const val MAX_RATING = 10

/**
 * Interactive 1–[MAX_RATING] rating: a row of stars filled in honey up to [rating]. The user can tap
 * a star or slide a finger across the row; each change gives a light haptic tick. TalkBack reads it
 * as an adjustable value ("Rating: 7 of 10") and can change it with volume / swipe gestures.
 *
 * @param rating Current rating, 0 means "not rated yet" (all stars empty).
 */
@Composable
fun RatingPicker(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current
    val description = stringResource(R.string.rating_value, rating, MAX_RATING)
    var widthPx by remember { mutableIntStateOf(0) }
    val currentRating by rememberUpdatedState(rating)
    val currentOnChange by rememberUpdatedState(onRatingChange)

    // Maps a touch x position to a star: the left edge of star N already selects N.
    fun updateFrom(x: Float) {
        if (widthPx == 0) return
        val newRating = ((x / widthPx) * MAX_RATING).toInt().plus(1).coerceIn(1, MAX_RATING)
        if (newRating != currentRating) {
            haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
            currentOnChange(newRating)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .onSizeChanged { widthPx = it.width }
            .pointerInput(Unit) { detectTapGestures { updateFrom(it.x) } }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(onDragStart = { updateFrom(it.x) }) { change, _ ->
                    change.consume()
                    updateFrom(change.position.x)
                }
            }
            .semantics(mergeDescendants = true) {
                contentDescription = description
                progressBarRangeInfo = ProgressBarRangeInfo(
                    current = rating.toFloat(),
                    range = 0f..MAX_RATING.toFloat(),
                    steps = MAX_RATING - 1,
                )
                setProgress { value ->
                    currentOnChange(value.roundToInt().coerceIn(1, MAX_RATING))
                    true
                }
            },
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        repeat(MAX_RATING) { index ->
            val filled = index < rating
            // Answers the touch: a selected star grows to full size.
            val scale by animateFloatAsState(targetValue = if (filled) 1f else 0.82f, label = "starScale")
            Icon(
                imageVector = if (filled) Icons.Rounded.Star else Icons.Rounded.StarOutline,
                contentDescription = null,
                tint = if (filled) KawTheme.colors.tagBorder else MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f)
                    .padding(vertical = 4.dp)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
            )
        }
    }
}

/**
 * Compact read-only rating, e.g. "★ 8/10", for cards in the Feed and the Journal. Neutral colors,
 * because on those screens the honey accent belongs to Kawos / the streak.
 */
@Composable
fun RatingLabel(
    rating: Int,
    modifier: Modifier = Modifier,
) {
    val description = stringResource(R.string.rating_value, rating, MAX_RATING)
    Row(
        modifier = modifier.semantics(mergeDescendants = true) { contentDescription = description },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
    ) {
        Icon(
            imageVector = Icons.Rounded.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp),
        )
        Text(
            text = stringResource(R.string.rating_fraction, rating, MAX_RATING),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}