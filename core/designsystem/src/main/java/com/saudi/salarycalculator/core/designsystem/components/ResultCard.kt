package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
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
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** One animated metric tile on the Result dashboard (Gross salary, GOSI employee/employer,
 * EOSB estimate, Net salary, ...). Deductions render in red with a leading minus sign. */
@Composable
fun ResultCard(
  label: String,
  value: Double,
  currencySymbol: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  accent: Color = BrandGreen,
  isDeduction: Boolean = false,
  icon: ImageVector? = null
) {
  GlassCard(darkMode = darkMode, modifier = modifier) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      icon?.let {
        androidx.compose.foundation.layout.Box(
          modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(accent.copy(alpha = 0.16f)),
          contentAlignment = Alignment.Center
        ) {
          Icon(it, contentDescription = null, tint = accent, modifier = Modifier.size(16.dp))
        }
        Spacer(Modifier.width(10.dp))
      }
      Text(
        label,
        style = MaterialTheme.typography.labelLarge,
        color = designSystemContentColor(darkMode).copy(alpha = 0.7f),
        fontWeight = FontWeight.SemiBold,
        maxLines = 2
      )
    }
    Row(verticalAlignment = Alignment.Bottom) {
      if (isDeduction) {
        Text("− ", color = BrandRed, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleLarge)
      }
      AnimatedCounter(
        targetValue = value,
        decimals = 0,
        style = MaterialTheme.typography.titleLarge,
        color = if (isDeduction) BrandRed else designSystemContentColor(darkMode)
      )
      Spacer(Modifier.width(4.dp))
      RiyalSymbol(
        tint = designSystemContentColor(darkMode).copy(alpha = 0.5f),
        size = 13.dp,
        contentDescription = currencySymbol,
        modifier = Modifier.padding(bottom = 3.dp)
      )
    }
  }
}
