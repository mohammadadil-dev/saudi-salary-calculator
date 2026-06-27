package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreenLight
import com.saudi.salarycalculator.core.designsystem.theme.designSystemContentColor

/** Linear progress bar + "Step X of Y" / step-name labels used at the top of the calculator
 * wizard. The fill animates smoothly between steps and mirrors correctly under RTL because it is
 * built from a start-aligned [Modifier.fillMaxWidth] fraction rather than manual offsets. */
@Composable
fun StepProgressIndicator(
  currentStep: Int,
  totalSteps: Int,
  stepLabel: String,
  stepOfLabel: String,
  darkMode: Boolean,
  modifier: Modifier = Modifier
) {
  val targetProgress = ((currentStep + 1).toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f)
  val progress by animateFloatAsState(
    targetValue = targetProgress,
    animationSpec = tween(450, easing = FastOutSlowInEasing),
    label = "step-progress"
  )
  Column(modifier = modifier.fillMaxWidth()) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
      Text(
        stepOfLabel,
        style = MaterialTheme.typography.labelMedium,
        color = designSystemContentColor(darkMode).copy(alpha = 0.6f),
        fontWeight = FontWeight.Bold
      )
      Text(stepLabel, style = MaterialTheme.typography.labelMedium, color = BrandGreen, fontWeight = FontWeight.Black)
    }
    Spacer(Modifier.height(8.dp))
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(8.dp)
        .clip(RoundedCornerShape(50))
        .background(if (darkMode) Color.White.copy(alpha = 0.1f) else Color(0xFFE7ECE8))
    ) {
      Box(
        modifier = Modifier
          .fillMaxWidth(progress)
          .fillMaxHeight()
          .clip(RoundedCornerShape(50))
          .background(Brush.horizontalGradient(listOf(BrandGreen, BrandGreenLight)))
      )
    }
  }
}
