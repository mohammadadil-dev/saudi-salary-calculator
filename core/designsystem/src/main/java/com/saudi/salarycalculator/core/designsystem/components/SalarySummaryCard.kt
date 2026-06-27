package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Home screen's headline card: last net salary as an animated count-up, or an empty-state
 * prompt when no calculation has been run yet. */
@Composable
fun SalarySummaryCard(
  title: String,
  netSalary: Double?,
  emptyText: String,
  currencySymbol: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier
) {
  GlassCard(darkMode = darkMode, modifier = modifier) {
    Text(
      title,
      style = MaterialTheme.typography.labelLarge,
      color = designSystemContentColor(darkMode).copy(alpha = 0.65f),
      fontWeight = FontWeight.Bold
    )
    if (netSalary == null) {
      Text(
        emptyText,
        style = MaterialTheme.typography.bodyMedium,
        color = designSystemContentColor(darkMode).copy(alpha = 0.55f),
        modifier = Modifier.padding(top = 2.dp)
      )
    } else {
      Row(verticalAlignment = Alignment.Bottom) {
        AnimatedCounter(
          targetValue = netSalary,
          decimals = 2,
          style = MaterialTheme.typography.headlineMedium,
          color = designSystemContentColor(darkMode)
        )
        Spacer(Modifier.width(6.dp))
        RiyalSymbol(
          tint = BrandGreen,
          size = 18.dp,
          contentDescription = currencySymbol,
          modifier = Modifier.padding(bottom = 6.dp)
        )
      }
    }
  }
}
