package pl.betoniarze.kawapp.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.ui.theme.KawTheme

@Immutable
data class KawTagColors(
    val container: Color,
    val content: Color,
    val border: Color,
)

object KawTagDefaults {
    /** Miodowy tag outlined – używany raz na ekran (chip metody, streak, Kawosy). */
    @Composable
    fun accent() = KawTagColors(
        container = KawTheme.colors.tagContainer,
        content = KawTheme.colors.onTag,
        border = KawTheme.colors.tagBorder,
    )

    /** Neutralny wariant tego samego komponentu (np. chip metody w Dzienniku). */
    @Composable
    fun neutral() = KawTagColors(
        container = MaterialTheme.colorScheme.surfaceContainerHighest,
        content = MaterialTheme.colorScheme.onSurface,
        border = Color.Transparent,
    )
}

/**
 * Jeden komponent tagu typu outlined/tonal.
 * Z [onClick] działa jako przycisk (np. Kawosy), bez – jako statyczna etykieta.
 */
@Composable
fun KawTag(
    text: String,
    modifier: Modifier = Modifier,
    colors: KawTagColors = KawTagDefaults.accent(),
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(horizontal = 8.dp, vertical = 3.dp),
    borderWidth: Dp = 1.5.dp,
    leading: (@Composable () -> Unit)? = null,
) {
    val shape = RoundedCornerShape(if (onClick != null) 20.dp else 6.dp)
    val border = if (colors.border == Color.Transparent) null else BorderStroke(borderWidth, colors.border)

    val content: @Composable () -> Unit = {
        Row(
            modifier = Modifier.padding(contentPadding),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            leading?.invoke()
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
            )
        }
    }

    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            color = colors.container,
            contentColor = colors.content,
            border = border,
            content = content,
        )
    } else {
        Surface(
            modifier = modifier,
            shape = shape,
            color = colors.container,
            contentColor = colors.content,
            border = border,
            content = content,
        )
    }
}
