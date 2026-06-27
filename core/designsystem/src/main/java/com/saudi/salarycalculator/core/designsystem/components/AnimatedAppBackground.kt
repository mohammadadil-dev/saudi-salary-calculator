package com.saudi.salarycalculator.core.designsystem.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.saudi.salarycalculator.core.designsystem.theme.BrandGold
import com.saudi.salarycalculator.core.designsystem.theme.BrandGreen
import com.saudi.salarycalculator.core.designsystem.theme.BrandTeal
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/** Slow, low-contrast wash that sits behind every screen so the app never feels static. Three
 * brand-colored glows (green/teal/gold) drift in a loose, looping orbit at ~0.1-0.18 alpha — kept
 * deliberately subtle so it never competes with foreground text for contrast. Lives behind
 * [androidx.compose.material3.Scaffold]'s content in [com.saudi.salarycalculator.feature.calculator.navigation.SalaryNavGraph],
 * which sets a transparent container color so this shows through. */
@Composable
fun AnimatedAppBackground(darkMode: Boolean, modifier: Modifier = Modifier) {
  val transition = rememberInfiniteTransition(label = "app-background")
  val phase by transition.animateFloat(
    initialValue = 0f,
    targetValue = (2 * Math.PI).toFloat(),
    animationSpec = infiniteRepeatable(
      animation = tween(durationMillis = 26000, easing = LinearEasing)
    ),
    label = "app-background-phase"
  )

  val baseColor = if (darkMode) Color(0xFF0A1612) else Color(0xFFF7F9F6)
  val glowAlpha = if (darkMode) 0.20f else 0.13f
  val glowColors = listOf(BrandGreen, BrandTeal, BrandGold)

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(baseColor)
      .drawBehind {
        val radius = hypot(size.width, size.height) * 0.45f
        glowColors.forEachIndexed { index, color ->
          val angle = phase + index * (2 * Math.PI / glowColors.size).toFloat()
          val cx = size.width * (0.5f + 0.42f * cos(angle))
          val cy = size.height * (0.32f + 0.28f * sin(angle))
          drawCircle(
            brush = Brush.radialGradient(
              colors = listOf(color.copy(alpha = glowAlpha), Color.Transparent),
              center = Offset(cx, cy),
              radius = radius
            ),
            radius = radius,
            center = Offset(cx, cy)
          )
        }
      }
  )
}
