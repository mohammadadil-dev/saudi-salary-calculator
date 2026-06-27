package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Small educational card used on the Home screen's "Salary insights" carousel/list (GOSI rate
 * explainer, EOSB growth, no personal income tax, etc.). */
@Composable
fun InsightCard(
  title: String,
  body: String,
  icon: ImageVector,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  accent: Color = BrandGreen
) {
  GlassCard(darkMode = darkMode, modifier = modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Box(
        modifier = Modifier
          .size(36.dp)
          .clip(CircleShape)
          .background(accent.copy(alpha = 0.16f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
      }
      Spacer(Modifier.width(10.dp))
      Text(
        title,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = designSystemContentColor(darkMode)
      )
    }
    Text(
      body,
      style = MaterialTheme.typography.bodySmall,
      color = designSystemContentColor(darkMode).copy(alpha = 0.65f)
    )
  }
}
