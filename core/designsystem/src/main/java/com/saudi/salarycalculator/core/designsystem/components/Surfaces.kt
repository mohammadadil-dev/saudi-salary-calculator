package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * The base "glassmorphism" surface used across Home/Result/Payslip/Comparison screens: a softly
 * translucent, rounded, lightly-bordered card. Every other card-shaped component in this package
 * (ResultCard, InsightCard, ComparisonCard, etc.) is built on top of this single primitive so the
 * elevation/blur/border treatment stays visually consistent app-wide.
 */
@Composable
fun GlassCard(
  darkMode: Boolean,
  modifier: Modifier = Modifier,
  cornerRadius: Int = 24,
  contentPadding: androidx.compose.foundation.layout.PaddingValues = androidx.compose.foundation.layout.PaddingValues(18.dp),
  verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(10.dp),
  content: @Composable ColumnScope.() -> Unit
) {
  val shape = RoundedCornerShape(cornerRadius.dp)
  Column(
    modifier = modifier
      .fillMaxWidth()
      .clip(shape)
      .background(
        Brush.linearGradient(
          if (darkMode) {
            listOf(Color.White.copy(alpha = 0.07f), Color.White.copy(alpha = 0.02f))
          } else {
            listOf(Color.White.copy(alpha = 0.92f), Color.White.copy(alpha = 0.62f))
          }
        )
      )
      .border(
        width = 1.dp,
        color = if (darkMode) Color.White.copy(alpha = 0.08f) else Color.White.copy(alpha = 0.95f),
        shape = shape
      )
      .padding(contentPadding),
    verticalArrangement = verticalArrangement,
    content = content
  )
}
