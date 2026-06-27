package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandRed
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** One labeled delta row on the Offer Comparison screen (net increase, % increase, GOSI/EOSB
 * difference, monthly/yearly impact). Positive deltas render in green, negative in red. */
@Composable
fun ComparisonCard(
  label: String,
  currentValue: String,
  newValue: String,
  deltaLabel: String,
  isPositive: Boolean,
  darkMode: Boolean,
  currencySymbol: String,
  modifier: Modifier = Modifier
) {
  GlassCard(darkMode = darkMode, modifier = modifier) {
    Text(
      label,
      style = MaterialTheme.typography.labelLarge,
      color = designSystemContentColor(darkMode).copy(alpha = 0.65f),
      fontWeight = FontWeight.Bold
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween) {
      Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(currentValue, style = MaterialTheme.typography.bodyMedium, color = designSystemContentColor(darkMode).copy(alpha = 0.55f))
          Spacer(Modifier.width(3.dp))
          RiyalSymbol(tint = designSystemContentColor(darkMode).copy(alpha = 0.55f), size = 11.dp, contentDescription = currencySymbol)
        }
      }
      Column(horizontalAlignment = Alignment.End) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(newValue, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = designSystemContentColor(darkMode))
          Spacer(Modifier.width(4.dp))
          RiyalSymbol(tint = designSystemContentColor(darkMode), size = 16.dp, contentDescription = currencySymbol)
        }
      }
    }
    HorizontalDivider(color = if (darkMode) Color.White.copy(alpha = 0.08f) else Color(0xFFE7ECE8))
    Row(verticalAlignment = Alignment.CenterVertically) {
      Text(
        deltaLabel,
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = if (isPositive) BrandGreen else BrandRed
      )
      Spacer(Modifier.width(3.dp))
      RiyalSymbol(tint = if (isPositive) BrandGreen else BrandRed, size = 11.dp, contentDescription = currencySymbol)
    }
  }
}
