package pl.betoniarze.kawapp.core.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.betoniarze.kawapp.core.ui.theme.KawTheme

/** Odstępy list jak w Feedzie: 12 dp od krawędzi, 8 dp między kartami, 12 dp w karcie. */
object KawListDefaults {
    val ContentPadding = PaddingValues(12.dp)
    val ItemSpacing = 8.dp
    val CardPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp)
}

@Composable
fun KawCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = KawListDefaults.CardPadding,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    val border = BorderStroke(1.dp, KawTheme.colors.cardBorder)
    val body: @Composable () -> Unit = {
        Column(
            modifier = Modifier.padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content,
        )
    }
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            color = KawTheme.colors.card,
            contentColor = MaterialTheme.colorScheme.onSurface,
            border = border,
            content = body,
        )
    } else {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = shape,
            color = KawTheme.colors.card,
            contentColor = MaterialTheme.colorScheme.onSurface,
            border = border,
            content = body,
        )
    }
}
