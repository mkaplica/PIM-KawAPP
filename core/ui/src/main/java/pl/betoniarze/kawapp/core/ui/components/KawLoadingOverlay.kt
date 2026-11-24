package pl.betoniarze.kawapp.core.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin
import pl.betoniarze.kawapp.core.ui.R
import pl.betoniarze.kawapp.core.ui.theme.KawTheme

/** How long the overlay takes to fade in or out. */
private const val OVERLAY_FADE_MS = 600
/** Opacity of the background color covering the content behind the overlay. */
private const val SCRIM_ALPHA = 0.9f

/**
 * Full-screen loading overlay: the screen background at [SCRIM_ALPHA] opacity covering data that is
 * not loaded yet, with an animated [SteamingCoffeeCup] in the center. It fades in and out slowly
 * and blocks touches while visible.
 *
 * @param animateFirstShow When false, an overlay that is already visible on its first composition
 * (e.g. after the Activity was recreated by a theme change) appears immediately instead of fading
 * in again.
 */
@Composable
fun KawLoadingOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier,
    animateFirstShow: Boolean = true,
) {
    // Start hidden unless animateFirstShow is false, see the KDoc above.
    val state = remember { MutableTransitionState(if (animateFirstShow) false else visible) }
    state.targetState = visible

    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(tween(OVERLAY_FADE_MS)),
        exit = fadeOut(tween(OVERLAY_FADE_MS)),
        modifier = modifier,
    ) {
        val description = stringResource(R.string.loading)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = SCRIM_ALPHA))
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        while (true) {
                            awaitPointerEvent().changes.forEach { it.consume() }
                        }
                    }
                }
                .semantics { contentDescription = description },
            contentAlignment = Alignment.Center,
        ) {
            SteamingCoffeeCup()
        }
    }
}

/**
 * A coffee cup on a saucer drawn on a Canvas, with three waving steam stripes that rise and pulse,
 * each with its own phase.
 */
@Composable
fun SteamingCoffeeCup(
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
) {
    val transition = rememberInfiniteTransition(label = "steam")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(1800, easing = LinearEasing), RepeatMode.Restart),
        label = "steamPhase",
    )

    val cupColor = MaterialTheme.colorScheme.primary
    val coffeeColor = KawTheme.colors.timerAccent
    val steamColor = MaterialTheme.colorScheme.onSurfaceVariant

    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Cup body: slightly tapered, rounded bottom.
        val left = w * 0.20f
        val right = w * 0.68f
        val top = h * 0.48f
        val bottom = h * 0.84f
        val taper = w * 0.04f
        val radius = w * 0.08f
        val body = Path().apply {
            moveTo(left, top)
            lineTo(right, top)
            lineTo(right - taper, bottom - radius)
            quadraticTo(right - taper, bottom, right - taper - radius, bottom)
            lineTo(left + taper + radius, bottom)
            quadraticTo(left + taper, bottom, left + taper, bottom - radius)
            close()
        }

        // Handle.
        val handleHeight = (bottom - top) * 0.55f
        drawArc(
            color = cupColor,
            startAngle = -90f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(right - taper * 2.5f, top + (bottom - top) * 0.12f),
            size = Size(w * 0.17f, handleHeight),
            style = Stroke(width = w * 0.05f, cap = StrokeCap.Round),
        )
        drawPath(body, color = cupColor)

        // Coffee surface.
        drawOval(
            color = coffeeColor,
            topLeft = Offset(left + w * 0.03f, top - h * 0.025f),
            size = Size(right - left - w * 0.06f, h * 0.05f),
        )

        // Saucer.
        drawRoundRect(
            color = cupColor,
            topLeft = Offset(w * 0.10f, bottom + h * 0.03f),
            size = Size(w * 0.68f, h * 0.045f),
            cornerRadius = CornerRadius(h * 0.03f),
        )

        // Three waving steam stripes, each with its own phase.
        val steamBottom = top - h * 0.07f
        val steamTop = h * 0.06f
        val length = steamBottom - steamTop
        val amplitude = w * 0.035f
        val segments = 24
        listOf(0.25f, 0.5f, 0.75f).forEachIndexed { index, fraction ->
            val x0 = left + (right - left) * fraction
            val offset = index * (2 * PI / 3).toFloat()
            val path = Path()
            for (s in 0..segments) {
                val t = s / segments.toFloat()
                val y = steamBottom - t * length
                // Waves travel upward; amplitude grows as the steam rises.
                val x = x0 + amplitude * (0.4f + 0.6f * t) *
                    sin(t * 2.2f * PI.toFloat() * 2f - phase + offset)
                if (s == 0) path.moveTo(x, y) else path.lineTo(x, y)
            }
            val alpha = 0.35f + 0.45f * (0.5f + 0.5f * sin(phase + offset))
            drawPath(
                path = path,
                color = steamColor.copy(alpha = alpha),
                style = Stroke(width = w * 0.035f, cap = StrokeCap.Round),
            )
        }
    }
}

